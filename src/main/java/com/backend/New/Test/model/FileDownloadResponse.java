package com.backend.New.Test.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileDownloadResponse {

    private String status;
    private int statusCode;
    private String msg;
}
