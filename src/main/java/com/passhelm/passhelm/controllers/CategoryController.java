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
import java.security.Principal;
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
    public ResponseEntity<List<Category>> getAllCategories(Principal principal, @Param("userId") Long userId) throws Exception{

        List<Category> categories = categoryService.getAllCategories(principal, userId);

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> createCategory(Principal principal, @RequestBody Category category,
                                                           UriComponentsBuilder uriComponentsBuilder) throws Exception {

        Category newCategory = categoryService.createCategory(principal, category);
        URI uri = uriComponentsBuilder.path("/category/{id}").buildAndExpand(newCategory.getId()).toUri();

        return ResponseEntity.created(uri).body(new CategoryResponse(newCategory));
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(Principal principal, @RequestBody Category category,
                                                           @PathVariable("id") Long id) throws Exception{
        Category updatedCategory = categoryService.updateCategory(principal, category, id);

        return ResponseEntity.ok(new CategoryResponse(updatedCategory));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity deleteCategory(
            Principal principal,
            @PathVariable("id") Long id
    ) throws Exception{

        categoryService.deleteCategory(principal, id);

        return ResponseEntity.noContent().build();
    }

}
