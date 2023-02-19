package com.passhelm.passhelm.validators.category;

import com.passhelm.passhelm.models.Category;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class ValidateIfCategoryHasEmptyProperties implements ValidateCategory {

    public void validate(Category category) {
        if(category.getLabel() == null || category.getLabel().isEmpty()) {
            throw new IllegalStateException("Label cannot be empty");
        }
        if(category.getColor() == null || category.getColor().isEmpty()) {
            throw new IllegalStateException("Color cannot be empty");
        }
        if(category.getUserId() == null) {
            throw new IllegalStateException("User Id cannot be empty");
        }
    }


    public void validate(Category category, Long userId) throws AccessDeniedException {
        throw new AccessDeniedException("Access denied");
    }
}
