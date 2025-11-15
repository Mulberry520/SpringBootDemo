package com.mulberry.service;

import com.mulberry.dto.ArticleDTO;
import com.mulberry.dto.ArticleSimpleDTO;
import com.mulberry.dto.ArticleUpdateDTO;

import java.util.List;

public interface ArticleService {
    List<ArticleSimpleDTO> getArticles(Integer userId);

    List<ArticleSimpleDTO> getArticle(Integer userId, int pageNum, int pageSize);

    ArticleDTO getArticleDetail(Integer userId, Integer articleId);

    int addArticle(ArticleDTO article);

    String updateArticle(ArticleUpdateDTO updates);

    String getCover(Integer articleId, Integer userId);

    int updateCover(Integer articleId, Integer userId, String coverUrl);
}
