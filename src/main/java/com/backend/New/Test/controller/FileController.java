package com.backend.New.Test.controller;

import com.backend.New.Test.model.DownloadData;
import com.backend.New.Test.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileService fileService;

//   @PostMapping("/addUser")
//    public Mono<ResponseEntity<DownloadData>> insertUser(){
//        return fileService.userData()
//                .flatMap(response -> Mono.just(ResponseEntity.ok().body(new DownloadData())))
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
}
