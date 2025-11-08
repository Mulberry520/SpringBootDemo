package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.dto.CategoryDTO;
import com.mulberry.dto.CategorySimpleDTO;
import com.mulberry.service.CategoryService;
import com.mulberry.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping
    public R<Void> addCategory(
            @RequestBody @Valid CategorySimpleDTO category,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        category.setCreateUserid(userService.getUserId(userDetails.getUsername()));
        categoryService.addCategory(category);
        return R.success();
    }

    @GetMapping
    public R<List<CategorySimpleDTO>> getCategory(@AuthenticationPrincipal UserDetails userDetails) {
        Integer userId = userService.getUserId(userDetails.getUsername());
        List<CategorySimpleDTO> categories = categoryService.getCategory(userId);
        return R.success(categories);
    }

    @GetMapping("/{id}")
    public R<CategoryDTO> getDetail(
            @PathVariable("id") Integer categoryId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        CategoryDTO category = categoryService.getCategoryDetail(userService.getUserId(username), categoryId);
        if (category == null) {
            return R.error();
        }

        category.setCreateUsername(username);
        return R.success(category);
    }

    @PatchMapping("/{id}")
    public R<String> updateCategory(
            @PathVariable("id") Integer categoryId,
            @RequestBody @Valid CategorySimpleDTO updates,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        updates.setId(categoryId);
        updates.setCreateUserid(userService.getUserId(userDetails.getUsername()));
        int affected = categoryService.updateCategory(updates);
        if (affected == 1) {
            return R.success();
        }
        return R.error("Not exists category or not belong to you");
    }

    @DeleteMapping("/{id}")
    public R<String> deleteCategory(
            @PathVariable("id") Integer categoryId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        int affected = categoryService.deleteCategory(userService.getUserId(userDetails.getUsername()), categoryId);
        if (affected == 1) {
            return R.success();
        }
        return R.error("Not exists category or not belong to you");
    }

}
