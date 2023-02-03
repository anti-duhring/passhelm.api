package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.PasswordRepository;
import com.passhelm.passhelm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository, UserRepository userRepository,
                           CategoryRepository categoryRepository) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Password> getAllPasswordsByUserId(Long userId) {
        return passwordRepository.findAllByUserId(userId);
    }

    public Password createPassword(Password password) {
        Optional<User> user = userRepository.findById(password.getUserId());
        Optional<Category> category = categoryRepository.findById(password.getCategoryId());

        if(user.isEmpty())  {
            throw new IllegalStateException("User not exist");
        }
        if(category.isEmpty())  {
            throw new IllegalStateException("Category not exist");
        }
        category.ifPresent(c -> {
            if(c.getUserId() != password.getUserId()) {
                throw new IllegalStateException("Category not belong to user");
            }
        });

        Password newPassword = passwordRepository.save(password);
        return newPassword;
    }
}
