package com.backend.New.Test.service;

import fi.solita.clamav.ClamAVClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ClamAvService {

    private static final String CLAMAV_HOST = "localhost";
    private static final int CLAMAV_PORT = 3310;

    public boolean isFileSafe(File file){
        ClamAVClient client = new ClamAVClient(CLAMAV_HOST,CLAMAV_PORT);
        try(FileInputStream input = new FileInputStream(file)){

            byte[] fileResponse = client.scan(input);
            return ClamAVClient.isCleanReply(fileResponse);

            } catch (IOException e){
            throw new RuntimeException("Error connecting to CLAMAV",e);
        }

    }
}
