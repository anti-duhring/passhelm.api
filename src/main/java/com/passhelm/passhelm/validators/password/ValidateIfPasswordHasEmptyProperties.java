package com.passhelm.passhelm.validators.password;

import com.passhelm.passhelm.models.Password;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfPasswordHasEmptyProperties implements PasswordValidator {

    public void validate(Password password) {
        if(password.getTitle() == null || password.getTitle().isEmpty()) {
            throw new IllegalStateException("Title cannot be empty");
        }
        if(password.getLogin() == null || password.getLogin().isEmpty()) {
            throw new IllegalStateException("Login cannot be empty");
        }
        if(password.getPassword() == null || password.getPassword().isEmpty()) {
            throw new IllegalStateException("Password cannot be empty");
        }
    }
}
