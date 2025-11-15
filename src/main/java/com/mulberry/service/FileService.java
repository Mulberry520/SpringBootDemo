package com.mulberry.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public enum Category {
        COVER("springboot-demo/cover/"),
        AVATAR("springboot-demo/avatar/"),
        RESOURCE("springboot-demo/resource/");

        private final String category;
        Category(String category) {
            this.category = category;
        }

        public String getCategory() {
            return this.category;
        }
    }


    String localSave(MultipartFile file) throws IOException;

    String ossSave(MultipartFile file, Category category) throws IOException;

    String generateSignedUrl(String originUrl);
}
