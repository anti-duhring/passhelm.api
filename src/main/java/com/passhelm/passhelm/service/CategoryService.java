package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {

        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {

        Optional<User> user = userRepository.findById(category.getUserId());

        if(category.getLabel() == null || category.getLabel().isEmpty()) {
            throw new IllegalStateException("Label cannot be empty");
        }
        if(category.getColor() == null || category.getColor().isEmpty()) {
            throw new IllegalStateException("Color cannot be empty");
        }
        if(category.getUserId() == null) {
            throw new IllegalStateException("User Id cannot be empty");
        }
        if(user.isEmpty()) {
            throw new IllegalStateException("User does not exist");
        }

        return categoryRepository.save(category);
    }
}
