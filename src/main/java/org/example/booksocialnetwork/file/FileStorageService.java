package org.example.booksocialnetwork.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booksocialnetwork.book.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static java.io.File.separator;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")

    private String fileUploadPath;

    public String saveFile(
           @NonNull MultipartFile sourceFile,
           @NonNull Book book,
           @NonNull Integer userId) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile,fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile,@NonNull String fileUploadSubPath) {

    final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
    File targetFolder = new File(finalUploadPath);
    if (!targetFolder.exists()) {
        boolean folderCreated = targetFolder.mkdirs();
        if(!folderCreated) {
            log.warn("Could not create folder ");
            return null;
        }
    }
    final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
    return null;
        }
        private String getFileExtension(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if(lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
    }

