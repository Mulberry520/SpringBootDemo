package com.mulberry.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.mulberry.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Value("${app.local-dir:uploads}")
    private String uploadDir;
    @Value("${oss.bucket-name}")
    private String bucketName;
    @Value("${oss.expiration}")
    private int expiration;

    private final OSS ossClient;

    public FileServiceImpl(OSS ossClient) {
        this.ossClient = ossClient;
    }

    private static final byte[][] IMAGE_MAGIC_BYTES = {
        // JPEG
        new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
        // PNG
        new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A},
        // GIF
        new byte[]{0x47, 0x49, 0x46, 0x38},
        // BMP
        new byte[]{0x42, 0x4D},
        // WebP
        new byte[]{0x52, 0x49, 0x46, 0x46, -1, -1, -1, -1, 0x57, 0x45, 0x42, 0x50}
    };

    private static boolean startsWith(byte[] data, byte[] magic) {
        if (data.length < magic.length) return false;
        for (int i = 0; i < magic.length; i++) {
            if (magic[i] != -1 && data[i] != magic[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = new byte[12];
            int read = inputStream.read(header);
            if (read < 2) return false;

            for (byte[] magic : IMAGE_MAGIC_BYTES) {
                if (startsWith(header, magic)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static String getRandomName(MultipartFile file) {
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
    public String ossSave(MultipartFile file, Category category) throws IOException {
        if (category == Category.AVATAR || category == Category.COVER) {
            if (!isImage(file)) {
                throw new IllegalArgumentException("This file is not an image");
            }
        }

        String savedName = category.getCategory() + getRandomName(file);
        PutObjectRequest objectRequest = new PutObjectRequest(this.bucketName, savedName, file.getInputStream());
        ossClient.putObject(objectRequest);

        return savedName;
    }

    @Override
    public String generateSignedUrl(String objectKey) {
        Date expirationTime = new Date(System.currentTimeMillis() + this.expiration);
        URL url = ossClient.generatePresignedUrl(bucketName, objectKey, expirationTime);
        return url.toString();
    }
}