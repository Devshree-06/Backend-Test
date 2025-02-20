package com.backend.New.Test.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonUtils {
    public static void logInfo(String content) {}
    public static void logInfo(String content,String reqId){}
    public static void errorInfo(String content,Throwable error){
        log.error(content + "-"+error);
    }
    public static void errorInfo(String reqId,String content,String error){
        log.error(reqId + "-" + content +"-"+ error);
    }
}
