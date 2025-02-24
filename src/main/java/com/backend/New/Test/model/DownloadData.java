package com.backend.New.Test.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Document(collection = "DownloadData")
@Builder
public class DownloadData {
    private ObjectId id;
    private String status;
    private String docType;
    private long fileSize;
    private LocalDateTime downloadedAt;
}
