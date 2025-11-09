package com.mulberry.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.mulberry.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Value("${app.local-dir:uploads}")
    private String uploadDir;
    @Value("${oss.bucket-name}")
    private String bucketName;
    @Value("${oss.target-url}")
    private String targetUrl;

    private final OSS ossClient;

    public FileServiceImpl(OSS ossClient) {
        this.ossClient = ossClient;
    }

    private String getRandomName(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty file");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Illegal filename");
        }

        String extension = "";
        int lastDot = filename.lastIndexOf(".");
        if (lastDot > 0) {
            extension = filename.substring(lastDot).toLowerCase();
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd_HHmmss_"));
        String randomId = UUID.randomUUID().toString().substring(0, 6);

        return  timestamp + randomId + extension;
    }

    @Override
    public String localSave(MultipartFile file) throws IOException {
        String savedName = getRandomName(file);

        File uploadDir = new File(this.uploadDir);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new RuntimeException("Can't create upload dir");
        }
        File destFile = new File(uploadDir, savedName);

        file.transferTo(destFile);
        return destFile.getName();
    }

    @Override
    public String ossSave(MultipartFile file) throws IOException {
        String savedName = getRandomName(file);

        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, savedName, file.getInputStream());
        ossClient.putObject(objectRequest);

        return targetUrl + savedName;
    }


}