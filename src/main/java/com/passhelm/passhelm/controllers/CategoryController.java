package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Category> getAllCategories(){

        return categoryService.getAllCategories();
    }

    @PostMapping("/category")
    public Category createCategory(@RequestBody Category category){

        return categoryService.createCategory(category);
    }
}
