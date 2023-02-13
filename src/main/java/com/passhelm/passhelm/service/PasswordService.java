package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.PasswordRepository;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository, UserRepository userRepository,
                           CategoryRepository categoryRepository, UserService userService) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public List<Password> getAllPasswordsByUserId(Principal principal, Long userId) throws Exception {
        User userPrincipal =
                userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException(
                        "User not found"));
        Boolean isAdmin = userService.isPrincipalAdmin(principal);

        if(!userPrincipal.getId().equals(userId) && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }

        return passwordRepository.findAllByUserId(userId);
    }

    public Password createPassword(Principal principal, Password password) throws Exception{
        User user = userRepository.findById(password.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Category category = categoryRepository.findById(password.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Boolean isAdmin = userService.isPrincipalAdmin(principal);

        if(category.getUserId() != password.getUserId()) {
            throw new IllegalStateException("Category not belong to user");
        }
        if(!user.getId().equals(password.getUserId()) && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }

        Password newPassword = passwordRepository.save(password);
        return newPassword;
    }

    @Transactional
    public Password updatePassword(Principal principal, Long passwordId, Password password) throws Exception {
        Password passwordToUpdate = passwordRepository.findById(passwordId).orElseThrow(() -> new EntityNotFoundException(
                "Password does not " +
                "exist"));
        Category category =
                categoryRepository.findById(password.getCategoryId()).orElseThrow(() -> new EntityNotFoundException(
                        "Category does not exist"));
        User userPrincipal = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Boolean isAdmin = userService.isPrincipalAdmin(principal);

        if(userPrincipal.getId() != passwordToUpdate.getUserId() && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
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

    public void deletePassword(Long passwordId) {

        Boolean passwordExists = passwordRepository.existsById(passwordId);

        if(!passwordExists) {
            throw new EntityNotFoundException("Password does not exist");
        }

        passwordRepository.deleteById(passwordId);
    }
}
