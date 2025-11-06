package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/file")
public class FileLoadController {
    private final FileService fileService;

    public FileLoadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/test")
    public String fileTest() {
        return "upload";
    }


    @PostMapping("/upload")
    @ResponseBody
    public R<String> uploadFile(
            @RequestParam("title") String title,
            @RequestParam("description") String desc,
            @RequestParam("file") MultipartFile file
    ) {
        System.out.println("title = " + title);
        System.out.println("desc = " + desc);

        try {
            fileService.saveFile(file);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }

        return R.success("File upload success");
    }

    @PostMapping("/uploads")
    @ResponseBody
    public R<String> multiUploadFile(
            @RequestParam("title") String title,
            @RequestParam("description") String desc,
            @RequestParam("files") MultipartFile[] files
    ) {
        System.out.println("title = " + title);
        System.out.println("desc = " + desc);

        for (MultipartFile file : files) {
            try {
                fileService.saveFile(file);
            } catch (Exception e) {
                return R.error(e.getMessage());
            }
        }
        return R.success("All files upload success");
    }
}
