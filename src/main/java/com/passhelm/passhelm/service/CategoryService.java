package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository, UserService userService) {

        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Category> getAllCategories(Long userId) throws Exception {

        if (userId == null) {
            throw new IllegalStateException("User Id cannot be empty");
        }

        List<Category> allCategories = categoryRepository.findAllByUserId(userId);

        return allCategories;
    }

    public Category createCategory(Principal principal, Category category) throws Exception {

        User user = userRepository.findById(category.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Boolean isAdmin = userService.isPrincipalAdmin(principal);

        if(category.getLabel() == null || category.getLabel().isEmpty()) {
            throw new IllegalStateException("Label cannot be empty");
        }
        if(category.getColor() == null || category.getColor().isEmpty()) {
            throw new IllegalStateException("Color cannot be empty");
        }
        if(category.getUserId() == null) {
            throw new IllegalStateException("User Id cannot be empty");
        }
        if(!user.getUsername().equals(principal.getName()) && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }


        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Principal principal, Category category, Long id) throws Exception {

        Category categoryToUpdate = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Category does not exist"));

        Boolean isAdmin = userService.isPrincipalAdmin(principal);
        User user =
                userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException());
        if(!isAdmin && !user.getId().equals(categoryToUpdate.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        if(category.getColor() != categoryToUpdate.getColor()) {
            categoryToUpdate.setColor(category.getColor());
        }
        if(category.getLabel()!= categoryToUpdate.getLabel()) {
            categoryToUpdate.setLabel(category.getLabel());
        }

        return categoryToUpdate;
    }

    public void deleteCategory(Principal principal, Long id) throws Exception {

        Category categoryToDelete = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Category does not exist"));

        Boolean isAdmin = userService.isPrincipalAdmin(principal);
        User user =
                userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException(
                        "User not found"));

        if(!isAdmin && !user.getId().equals(categoryToDelete.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }


        categoryRepository.deleteById(id);
    }
}
