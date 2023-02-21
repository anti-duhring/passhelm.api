package com.passhelm.passhelm.records;

import com.passhelm.passhelm.models.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record PasswordResponse(Long id, Long userId, Long categoryId, String title, String login,
                               String password) {


    public PasswordResponse(Password password) {
        this(password.getId(), password.getUserId(), password.getCategoryId(), password.getTitle(),
                password.getLogin(), password.getPassword());
    }

}