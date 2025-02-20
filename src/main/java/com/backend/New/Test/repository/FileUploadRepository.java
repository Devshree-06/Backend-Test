package com.backend.New.Test.repository;

import com.backend.New.Test.model.UploadFile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FileUploadRepository extends ReactiveMongoRepository<UploadFile,String> {

    Mono<UploadFile> findByDocPath(String docPath);
}
