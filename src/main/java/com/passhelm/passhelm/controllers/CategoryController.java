package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public List<Category> getAllCategories(@Param("userId") Long userId) {

        return categoryService.getAllCategories(userId);
    }

    @PostMapping("/category")
    public Category createCategory(@RequestBody Category category){

        return categoryService.createCategory(category);
    }

    @PutMapping("/category/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable("id") Long id){

        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(@PathVariable("id") Long id){

        categoryService.deleteCategory(id);
    }

}
