package com.backend.New.Test.controller;

import com.backend.New.Test.model.DownloadData;
import com.backend.New.Test.model.UploadFile;
import com.backend.New.Test.model.UploadFileResponse;
import com.backend.New.Test.service.FileService;
//import com.backend.New.Test.service.RabbitMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    FileService fileService;

//    @Autowired
//    RabbitMQProducer producer;

    @PostMapping("/upload")
    public Mono<ResponseEntity<UploadFileResponse>> uploadedFile(@RequestParam("docPath")String docPath){
        log.info("uploadFile api called");
        return fileService.uploadFile(docPath)
                .doOnNext(response -> log.info("Response: " + response.getBody()))
                .doOnError(error -> log.error("Error during file upload", error));
    }

//    @PostMapping("/sendFileMessage")
//    public ResponseEntity<String> sendMessage(@RequestParam("message") String message){
//        producer.sendFileMessage(message);
//        return ResponseEntity.ok("File test message is send to RabbitMQ..");
//    }
}
