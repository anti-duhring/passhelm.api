package com.passhelm.passhelm.validators.user;

import com.passhelm.passhelm.models.User;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Component
public class ValidateIfUserHasEmptyProperties implements ValidateUserActions{
    public ValidateIfUserHasEmptyProperties() {
    }

    public void validate(Principal principal) throws Exception {
        throw new AccessDeniedException("Access Denied");
    }

    public void validate(Principal principal, Long userId) throws Exception {
        throw new AccessDeniedException("Access Denied");
    }

    public void validate(User user, String password) throws Exception {
        throw new AccessDeniedException("Access Denied");
    }

    public void validate(User user) throws Exception {

        if(user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalStateException("User must have a username");
        }
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalStateException("User must have a password");
        }
        if(user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalStateException("User must have a name");
        }
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalStateException("User must have an email");
        }
    }


}
