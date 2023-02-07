package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.records.CategoryResponse;
import com.passhelm.passhelm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<List<Category>> getAllCategories(@Param("userId") Long userId) {

        List<Category> categories = categoryService.getAllCategories(userId);

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody Category category, UriComponentsBuilder uriComponentsBuilder){

        Category newCategory = categoryService.createCategory(category);
        URI uri = uriComponentsBuilder.path("/category/{id}").buildAndExpand(newCategory.getId()).toUri();

        return ResponseEntity.created(uri).body(new CategoryResponse(newCategory));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody Category category, @PathVariable("id") Long id){
        Category updatedCategory = categoryService.updateCategory(category, id);

        return ResponseEntity.ok(new CategoryResponse(updatedCategory));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity deleteCategory(@PathVariable("id") Long id){

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }

}
