package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileLoadController {
    private final FileService fileService;

    public FileLoadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/local")
    public R<String> uploadToLocal(
            @RequestParam("file") MultipartFile file
    ) {
        String filePath;

        try {
            filePath = fileService.localSave(file);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }

        return R.success(filePath);
    }

    @PostMapping("/cloud")
    public R<String> uploadToCloud(
            @RequestParam("file") MultipartFile file
    ) {
        String fileUrl;

        try {
            fileUrl = fileService.ossSave(file);
        } catch (IOException e) {
            return R.error(e.getMessage());
        }

        return R.success(fileUrl);
    }


}
