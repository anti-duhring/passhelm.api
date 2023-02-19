package com.passhelm.passhelm.validators.category;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

@Component
public class ValidaIfCategoryBelongToUser implements ValidateCategory{

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ValidaIfCategoryBelongToUser(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public void validate(Category category) throws AccessDeniedException {
        throw new AccessDeniedException("Access denied");
    }

    public void validate(Category category, Long userId) throws AccessDeniedException {
        Category categoryEntity =
                categoryRepository.findById(category.getId()).orElseThrow(() -> new EntityNotFoundException("Category not " +
                        "found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(categoryEntity.getUserId() != user.getId()) {
            throw new IllegalStateException("Category not belong to user");
        }
    }
}
