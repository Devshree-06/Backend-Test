package com.backend.New.Test.service;

import com.backend.New.Test.model.DownloadData;
import com.backend.New.Test.model.FileDownloadResponse;
import com.backend.New.Test.repository.FileSystemRepository;
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

@Service
public class FileService {

    @Autowired
    ClamAvService clamAvService;

    @Autowired
    FileSystemRepository fileSystemRepository;
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

//    public Mono<ResponseEntity<FileDownloadResponse>> userData(){
//        String userName = "abc";
//
//        DownloadData user = new DownloadData();
//        user.setUserName(userName);
//
//        return fileSystemRepository.save(user)
//                .flatMap(result->{
//                    FileDownloadResponse response = new FileDownloadResponse(
//                            "success",
//                            200,
//                            "User data saved successfully"
//                    );
//                    return Mono.just(ResponseEntity.ok(response));
//                })
//                .doOnSuccess(saved-> System.out.println("Data saved to database"))
//                .doOnError(error-> System.out.println("Error saving data to database"));
//
//    }
}

