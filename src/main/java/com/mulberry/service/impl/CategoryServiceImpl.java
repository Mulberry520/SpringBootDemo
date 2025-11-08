package com.mulberry.service.impl;

import com.mulberry.dto.CategoryDTO;
import com.mulberry.dto.CategorySimpleDTO;
import com.mulberry.mapper.CategoryMapper;
import com.mulberry.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int addCategory(CategorySimpleDTO category) {
        return mapper.insertCategory(
                category.getCategoryName(),
                category.getCategoryAlias(),
                category.getCreateUserid()
        );
    }

    @Override
    public List<CategorySimpleDTO> getCategory(Integer userId) {
        return mapper.selectByCreator(userId);
    }

    @Override
    public CategoryDTO getCategoryDetail(Integer userId, Integer categoryId) {
        return mapper.selectById(userId, categoryId);
    }

    @Override
    public int updateCategory(CategorySimpleDTO updates) {
        return mapper.updateCategory(
                updates.getCategoryName(),
                updates.getCategoryAlias(),
                updates.getCreateUserid(),
                updates.getId()
        );
    }

    @Override
    public int deleteCategory(Integer userId, Integer categoryId) {
        return mapper.deleteCategory(userId, categoryId);
    }
}
