package com.backend.New.Test.repository;

import com.backend.New.Test.model.DownloadData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FileSystemRepository extends ReactiveMongoRepository<DownloadData, String> {

//    Mono<DownloadData> userData(String userName);
}
