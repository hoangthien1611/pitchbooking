package com.hoangthien.pitchbooking.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    private Path fileStorageLocation;

    public FileServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
        if(!this.fileStorageLocation.toFile().exists()) {
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }


    @Override
    public String saveFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename()).replace(" ", "");
        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                return "";
            }

            // Copy file to the target location (Replacing existing file with the same name - it's not gonna happen)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
