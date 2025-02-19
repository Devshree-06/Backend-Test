package com.backend.New.Test.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Document(collection = "DownloadData")
public class DownloadData {
    private ObjectId id;
    private String userName;
}
