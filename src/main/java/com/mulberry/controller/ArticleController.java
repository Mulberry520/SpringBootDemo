package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.dto.ArticleDTO;
import com.mulberry.dto.ArticleSimpleDTO;
import com.mulberry.dto.ArticleUpdateDTO;
import com.mulberry.service.ArticleService;
import com.mulberry.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
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
    public R<String> updateArticle(
            @PathVariable("id") Integer articleId,
            @RequestBody @Valid ArticleUpdateDTO updates,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updates.setId(articleId);
        updates.setCreateUser(userService.getUserId(userDetails.getUsername()));

        String errInfo = articleService.updateArticle(updates);
        if (errInfo == null) {
            return R.success();
        }
        return R.error(errInfo);
    }
}
