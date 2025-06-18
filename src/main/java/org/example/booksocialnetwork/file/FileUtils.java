package org.example.booksocialnetwork.file;

import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {

    public static byte[] readFileFromLocation(String fileurl){
        if(StringUtils.isBlank(fileurl)){
            return null;
        }
        try{
            Path filePath = new File(fileurl).toPath();
            return Files.readAllBytes(filePath);
        }catch (IOException e){
            log.warn("No file found in the path {}", fileurl);
        }
        return null;

    }
}
