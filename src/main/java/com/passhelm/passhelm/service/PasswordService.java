package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.PasswordRepository;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Password updatePassword(Long passwordId, Password password) {
        Password passwordToUpdate = passwordRepository.findById(passwordId).orElseThrow(() -> new IllegalStateException(
                "Password does not " +
                "exist"));
        Category category =
                categoryRepository.findById(password.getCategoryId()).orElseThrow(() -> new IllegalStateException(
                        "Category does not exist"));

        if(category.getUserId()!= passwordToUpdate.getUserId()) {
            throw new IllegalStateException("Category not belong to user");
        }

        if(password.getCategoryId()!= passwordToUpdate.getCategoryId()) {
            passwordToUpdate.setCategoryId(password.getCategoryId());
        }
        if(
                password.getPassword()!= null &&
                password.getPassword().length() > 0 &&
                password.getPassword() != passwordToUpdate.getPassword()
        ) {
            passwordToUpdate.setPassword(password.getPassword());
        }
        if(
                password.getLogin()!= null &&
                password.getLogin().length() > 0 &&
                password.getLogin()!= passwordToUpdate.getLogin()
        ) {
            passwordToUpdate.setLogin(password.getLogin());
        }
        if(
                password.getTitle()!= null &&
                password.getTitle().length() > 0 &&
                password.getTitle()!= passwordToUpdate.getTitle()
        ) {
            passwordToUpdate.setTitle(password.getTitle());
        }

        return passwordToUpdate;
    }
}
