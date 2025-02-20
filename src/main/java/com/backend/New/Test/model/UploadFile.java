package com.backend.New.Test.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "UploadFile")
public class UploadFile {
    private ObjectId id;
    private  String docId;
    private String status;
    private String docType;
    private String docPath;
    private LocalDateTime createdAt;
}
