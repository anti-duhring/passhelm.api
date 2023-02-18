package com.passhelm.passhelm.validators.category;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ValidaIfCategoryBelongToUser implements ValidateCategory{

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ValidaIfCategoryBelongToUser(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public void validate(Category category) {

    }

    public void validate(Category category, Long userId) {
        Category categoryEntity =
                categoryRepository.findById(category.getId()).orElseThrow(() -> new EntityNotFoundException("Category not " +
                        "found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(categoryEntity.getUserId() != user.getId()) {
            throw new IllegalStateException("Category not belong to user");
        }
    }
}
