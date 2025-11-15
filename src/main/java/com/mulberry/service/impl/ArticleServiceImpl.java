package com.mulberry.service.impl;

import com.mulberry.dto.ArticleDTO;
import com.mulberry.dto.ArticleSimpleDTO;
import com.mulberry.dto.ArticleUpdateDTO;
import com.mulberry.mapper.ArticleMapper;
import com.mulberry.mapper.CategoryMapper;
import com.mulberry.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper mapper;
    private final CategoryMapper categoryMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper, CategoryMapper categoryMapper) {
        this.mapper = articleMapper;
        this.categoryMapper = categoryMapper;
    }
    @Override
    public List<ArticleSimpleDTO> getArticles(Integer userId) {
        return mapper.selectAllByCreator(userId);
    }

    @Override
    public List<ArticleSimpleDTO> getArticle(Integer userId, int pageNum, int pageSize) {
        if (pageSize <= 0 || pageNum <= 0) {
            throw new IllegalArgumentException("PageNum or pageSize should be positive");
        }
        int offset = (pageNum - 1) * pageSize;
        return mapper.selectSomeByCreator(userId, offset, pageSize);
    }

    @Override
    public int addArticle(ArticleDTO article) {
        Integer existsId = categoryMapper.selectIdById(article.getCategoryId());
        if (existsId == null) {
            throw new IllegalArgumentException("Not existed category");
        }
        return mapper.insertArticle(
                article.getTitle(),
                article.getContent(),
                article.getCoverImg(),
                article.getState(),
                article.getCategoryId(),
                article.getCreateUser()
        );
    }

    @Override
    public String updateArticle(ArticleUpdateDTO updates) {
        Integer articleId = updates.getId();
        Integer userId = updates.getCreateUser();
        ArticleDTO targetArticle = mapper.selectDetailById(userId, articleId);
        if (targetArticle == null) {
            return "Article not exists or not belongs to you";
        }

        Integer newCategory = updates.getCategoryId();
        if (newCategory != null) {
            Integer existsCategory = categoryMapper.selectIdById(newCategory);
            if (existsCategory == null) {
                return "Not exists this category";
            }
            targetArticle.setCategoryId(newCategory);
        }
        String newTitle = updates.getTitle();
        String newContent = updates.getContent();
        String newState = updates.getState();
        if (newTitle != null) {
            targetArticle.setTitle(newTitle);
        }
        if (newContent != null) {
            targetArticle.setContent(newContent);
        }
        if (newState != null) {
            targetArticle.setState(newState);
        }

        int affected = mapper.updateArticle(
                targetArticle.getTitle(),
                targetArticle.getContent(),
                targetArticle.getCoverImg(),
                targetArticle.getState(),
                targetArticle.getCategoryId(),
                articleId
        );
        if (affected == 1) {
            return null;
        }
        return "Update article failed";
    }

    @Override
    public String getCover(Integer articleId, Integer userId) {
        return mapper.selectCoverByIdAndName(articleId, userId);
    }

    @Override
    public int updateCover(Integer articleId, Integer userId, String coverUrl) {
        return mapper.updateCoverByIdAndName(articleId, userId, coverUrl);
    }

    @Override
    public ArticleDTO getArticleDetail(Integer userId, Integer articleId) {
        return mapper.selectDetailById(userId, articleId);
    }
}
