package com.mulberry.controller;

import com.mulberry.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    @GetMapping("/1")
    public R<Void> hello() {
        return R.success("this is article page");
    }

}
