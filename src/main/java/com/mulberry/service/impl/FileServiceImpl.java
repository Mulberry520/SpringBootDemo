package com.mulberry.service.impl;

import com.mulberry.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }

        Path rootLocation = Paths.get(this.uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Can't create upload directory");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Illegal file name");
        }
        String extension = "";
        int lastDot = filename.lastIndexOf(".");
        if (lastDot > 0) {
            extension = filename.substring(lastDot);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd_HHmmss_"));
        String randomId = UUID.randomUUID().toString().substring(0, 6);
        String savedName = timestamp + randomId + extension;
        Path destination = rootLocation.resolve(savedName);
        Files.copy(file.getInputStream(), destination);
    }
}