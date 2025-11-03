package com.mulberry.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    void saveFile(MultipartFile file) throws IOException;
}
