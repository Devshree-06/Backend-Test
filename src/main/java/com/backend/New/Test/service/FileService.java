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

    //User download file api method
    public Mono<ResponseEntity<?>> fileDownload(String filepath){
        File file = new File(filepath);

        if(!clamAvService.isFileSafe(file)){
            return Mono.just(ResponseEntity.ok(FileDownloadResponse.builder()
                    .status("fail")
                    .statusCode(100)
                    .msg("File is corrupted")
                    .build()));
        }

        FileSystemResource resource = new FileSystemResource(file);
        return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource));
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

                String doc_id = UUID.randomUUID().toString(); //random doc id created for every new file uploaded
                UploadFile uploadFile = new UploadFile();
                uploadFile.setDocId(doc_id);
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

