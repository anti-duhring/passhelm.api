package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.UserRepository;
import com.passhelm.passhelm.validators.user.ValidateIfIsTheSameUserOrAdmin;
import com.passhelm.passhelm.validators.user.ValidateIfUserIsAdmin;
import com.passhelm.passhelm.validators.category.ValidateIfCategoryHasEmptyProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ValidateIfUserIsAdmin validateIfUserIsAdmin;
    private final ValidateIfIsTheSameUserOrAdmin validateIfIsTheSameUserOrAdmin;
    private final ValidateIfCategoryHasEmptyProperties validateIfCategoryHasEmptyProperties;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository, UserService userService, ValidateIfUserIsAdmin validateIfUserIsAdmin, ValidateIfIsTheSameUserOrAdmin validateIfIsTheSameUserOrAdmin, ValidateIfCategoryHasEmptyProperties validateIfCategoryHasEmptyProperties) {

        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.validateIfUserIsAdmin = validateIfUserIsAdmin;
        this.validateIfIsTheSameUserOrAdmin = validateIfIsTheSameUserOrAdmin;
        this.validateIfCategoryHasEmptyProperties = validateIfCategoryHasEmptyProperties;
    }

    public List<Category> getAllCategories(Principal principal, Long userId) throws Exception {
        validateIfIsTheSameUserOrAdmin.validate(principal, userId);
        if (userId == null) {
            throw new IllegalStateException("User Id cannot be empty");
        }

        List<Category> allCategories = categoryRepository.findAllByUserId(userId);

        return allCategories;
    }

    public Category createCategory(Principal principal, Category category) throws Exception {

        validateIfIsTheSameUserOrAdmin.validate(principal, category.getUserId());
        validateIfCategoryHasEmptyProperties.validate(category);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Principal principal, Category category, Long id) throws Exception {

        Category categoryToUpdate = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Category does not exist"));

        validateIfIsTheSameUserOrAdmin.validate(principal, categoryToUpdate.getUserId());

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

        validateIfIsTheSameUserOrAdmin.validate(principal, categoryToDelete.getUserId());



        categoryRepository.deleteById(id);
    }
}
