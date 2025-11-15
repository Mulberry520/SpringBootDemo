package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.dto.ArticleDTO;
import com.mulberry.dto.ArticleSimpleDTO;
import com.mulberry.dto.ArticleUpdateDTO;
import com.mulberry.service.ArticleService;
import com.mulberry.service.FileService;
import com.mulberry.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;
    private final FileService fileService;

    public ArticleController(
            ArticleService articleService,
            UserService userService,
            FileService fileService
    ) {
        this.articleService = articleService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @GetMapping("/all")
    public R<List<ArticleSimpleDTO>> articleList(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        List<ArticleSimpleDTO> articles = articleService.getArticles(userService.getUserId(username));
        if (articles == null) {
            return R.error();
        }

        return R.success(articles);
    }

    @GetMapping
    public R<List<ArticleSimpleDTO>> getArticles(
            @RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer userId = userService.getUserId(userDetails.getUsername());
        List<ArticleSimpleDTO> articles = articleService.getArticle(userId, pageNum, pageSize);
        return R.success(articles);
    }

    @PostMapping
    public R<Void> addArticle(
            @RequestBody @Valid ArticleDTO article,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        article.setCreateUser(userService.getUserId(userDetails.getUsername()));
        articleService.addArticle(article);
        return R.success();
    }

    @GetMapping("/{id}")
    public R<ArticleDTO> getDetail(
            @PathVariable("id") Integer articleId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        ArticleDTO article = articleService.getArticleDetail(userService.getUserId(username), articleId);
        if (article == null) {
            return R.error("Article not exists or not belong to you");
        }

        article.setCreateUsername(username);
        return R.success(article);
    }

    @PatchMapping("/{id}")
    public R<Void> updateArticle(
            @PathVariable("id") Integer articleId,
            @RequestBody ArticleUpdateDTO updates,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updates.setId(articleId);
        updates.setCreateUser(userService.getUserId(userDetails.getUsername()));
        articleService.updateArticle(updates);
        return R.success();
    }

    @GetMapping("/{id}/cover")
    public R<String> getCover(
            @PathVariable("id") Integer articleId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String coverUrl = articleService.getCover(articleId, userService.getUserId(userDetails.getUsername()));
        if (coverUrl == null || coverUrl.length() < 20) {
            return R.error("You haven't post a valid cover");
        }

        String signedCover = fileService.generateSignedUrl(coverUrl);
        return R.success("This is your cover's temp url", signedCover);
    }

    @PatchMapping("/{id}/cover")
    public R<String> updateCover(
            @PathVariable("id") Integer articleId,
            @RequestParam("file") MultipartFile cover,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String coverUrl = fileService.ossSave(cover, FileService.Category.COVER);
            articleService.updateCover(articleId, userService.getUserId(userDetails.getUsername()), coverUrl);
            return R.success();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
