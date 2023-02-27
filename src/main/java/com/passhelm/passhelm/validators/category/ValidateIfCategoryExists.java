package com.passhelm.passhelm.validators.category;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class ValidateIfCategoryExists implements ValidateCategory{

    private final CategoryRepository categoryRepository;

    public ValidateIfCategoryExists(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validate(Category category) throws Exception {
        categoryRepository.findById(category.getId()).orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
    }


    public void validate(Category category, Long userId) throws AccessDeniedException {
        throw new AccessDeniedException("Access denied");
    }
}
