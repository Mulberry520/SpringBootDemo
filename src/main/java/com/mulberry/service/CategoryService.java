package com.mulberry.service;

import com.mulberry.dto.CategoryDTO;
import com.mulberry.dto.CategorySimpleDTO;

import java.util.List;

public interface CategoryService {
    int addCategory(CategorySimpleDTO category);

    List<CategorySimpleDTO> getCategory(Integer userId);

    CategoryDTO getCategoryDetail(Integer userId, Integer categoryId);

    int updateCategory(CategorySimpleDTO updates);

    int deleteCategory(Integer userId, Integer categoryId);


}
