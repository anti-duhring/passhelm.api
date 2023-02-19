package com.passhelm.passhelm.validators.user;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Component
public class ValidateIfUserIsAdmin implements ValidateUserActions{

    @Autowired
    private final UserRepository userRepository;

    public ValidateIfUserIsAdmin(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void validate(Principal principal) throws Exception {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException(
                "User does not " + "exist"));
        Boolean isPrincipalAdmin = user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if(!isPrincipalAdmin) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void validate(Principal principal, Long userId) throws Exception {
        throw new AccessDeniedException("Access denied");
    }
}
