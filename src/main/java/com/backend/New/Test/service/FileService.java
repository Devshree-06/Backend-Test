package com.backend.New.Test.service;

import com.backend.New.Test.model.DownloadData;
import com.backend.New.Test.model.FileDownloadResponse;
import com.backend.New.Test.model.UploadFile;
import com.backend.New.Test.model.UploadFileResponse;
import com.backend.New.Test.repository.FileSystemRepository;
import com.backend.New.Test.repository.FileUploadRepository;
import com.backend.New.Test.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.xml.transform.Source;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    ClamAvService clamAvService;

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Autowired
    CommonUtils commonUtils;


    //User download file api
    public Mono<ResponseEntity<?>> fileDownload(String filepath){
        File file = new File(filepath);

        //search by doc path for the document and if present, download the file.
        return fileUploadRepository.findByDocPath(filepath)
                .flatMap(fileExists-> {
                    if (fileExists == null) {
                        return Mono.just(ResponseEntity.ok(FileDownloadResponse.builder()
                                .status("fail")
                                .statusCode(100)
                                .msg("The file does not exists")
                                .build()));
                    }
                    if (!clamAvService.isFileSafe(file)) {
                        return Mono.just(ResponseEntity.ok(FileDownloadResponse.builder()
                                .status("fail")
                                .statusCode(100)
                                .msg("The file is corrupted. Cannot be downloaded")
                                .build()));
                    }

                    Path fileType = file.toPath();
                    String fileContent;
                    try {
                        fileContent = Files.probeContentType(fileType);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    FileSystemResource fileDownload = new FileSystemResource(file);
                    DownloadData downloadData = new DownloadData();
                    downloadData.setStatus("File Downloaded");
                    downloadData.setDocType(fileContent);
                    downloadData.setFileSize(file.length());
                    downloadData.setDownloadedAt(LocalDateTime.now());

                    return Mono.just(ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION,"attatchment;filename=\""+ file.getName() + "\"")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(fileDownload));
                });

    }

    //User upload file api
    public Mono<ResponseEntity<UploadFileResponse>> uploadFile(String filepath) {
        return Mono.defer(() -> {
            try {
                File file = new File(filepath);
                CommonUtils.logInfo("The file path is : "+filepath);
                if (!file.exists()) {
                    return Mono.just(new ResponseEntity<>(
                            new UploadFileResponse("fail", 100, "File does not exist"), HttpStatus.OK));
                }

//                Integer doc_id = Integer.valueOf(String.valueOf(UUID.randomUUID())); //random doc id created for every new file uploaded
                String docId = UUID.randomUUID().toString();
                UploadFile uploadFile = new UploadFile();
                uploadFile.setDocId(docId);
                uploadFile.setStatus("Uploaded");

                Path filePath = file.toPath();
                String fileContent = Files.probeContentType(filePath);
                if (fileContent == null) {
                    fileContent = "application/octet-stream";
                }
                uploadFile.setDocType(fileContent);
                uploadFile.setDocPath(filepath);
                uploadFile.setCreatedAt(LocalDateTime.now());

                return fileUploadRepository.findByDocPath(filepath)
                        .flatMap(existingFile->{
                            return Mono.just(new ResponseEntity<>(new UploadFileResponse("fail",102,"File already exists"),HttpStatus.OK));
                        })
                        .switchIfEmpty(
                                fileUploadRepository.save(uploadFile)
                        .map(uploaded -> new ResponseEntity<>(
                                new UploadFileResponse("success", 200, "File uploaded successfully"), HttpStatus.OK))
                        .onErrorResume(error -> {
                            CommonUtils.errorInfo("Error while uploading file", filepath, error.getMessage());
                            return Mono.just(new ResponseEntity<>(
                                    new UploadFileResponse("fail", 101, "Error uploading file"), HttpStatus.INTERNAL_SERVER_ERROR));
                        }));
            } catch (IOException e) {
                return Mono.error(new RuntimeException("IOException occurred: " + e.getMessage(), e));
            }
        });
    }

}

