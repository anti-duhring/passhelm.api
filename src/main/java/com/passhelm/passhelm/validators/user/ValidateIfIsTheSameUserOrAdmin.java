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
public class ValidateIfIsTheSameUserOrAdmin implements ValidateUserActions{

    @Autowired
    private final UserRepository userRepository;

    public ValidateIfIsTheSameUserOrAdmin(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void validate(Principal principal) throws Exception {

    }

    public void validate(Principal principal, Long userId) throws Exception{
        Boolean isPrincipalAdmin = this.isPrincipalAdmin(principal);
        User principalUser = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(!isPrincipalAdmin && !principalUser.getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to perform this action");
        }

    }

    private Boolean isPrincipalAdmin(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException(
                "User does not " + "exist"));

        return user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
