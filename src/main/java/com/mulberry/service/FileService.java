package com.mulberry.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String localSave(MultipartFile file) throws IOException;

    String ossSave(MultipartFile file) throws IOException;
}
