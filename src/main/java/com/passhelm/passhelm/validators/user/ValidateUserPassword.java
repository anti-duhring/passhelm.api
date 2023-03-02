package com.passhelm.passhelm.validators.user;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Component
public class ValidateUserPassword implements ValidateUserActions{

    private UserRepository userRepository;

    public ValidateUserPassword(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(Principal principal) throws Exception {
        throw new AccessDeniedException("Access denied");
    }

    public void validate(Principal principal, Long userId) throws Exception {
        throw new AccessDeniedException("Access denied");
    }

    public void validate(User user, String password) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(password, user.getPassword())){
            throw new AccessDeniedException("Invalid password");
        }
    }

    public void validate(User user) throws Exception {
        User userData = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!encoder.matches(user.getPassword(), userData.getPassword())){
            throw new AccessDeniedException("Invalid password");
        }
    }

}
