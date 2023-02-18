package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.Category;
import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.repository.CategoryRepository;
import com.passhelm.passhelm.repository.PasswordRepository;
import com.passhelm.passhelm.repository.UserRepository;
import com.passhelm.passhelm.validators.password.ValidateIfPasswordHasEmptyProperties;
import com.passhelm.passhelm.validators.user.ValidateIfIsTheSameUserOrAdmin;
import com.passhelm.passhelm.validators.user.ValidateIfUserIsAdmin;
import com.passhelm.passhelm.validators.category.ValidaIfCategoryBelongToUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PasswordService {
    private final PasswordRepository passwordRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final ValidateIfIsTheSameUserOrAdmin validateIfIsTheSameUserOrAdmin;
    private final ValidateIfUserIsAdmin validateIfUserIsAdmin;
    private final ValidaIfCategoryBelongToUser validaIfCategoryBelongToUser;
    private final ValidateIfPasswordHasEmptyProperties validateIfPasswordHasEmptyProperties;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository, UserRepository userRepository,
                           CategoryRepository categoryRepository, UserService userService, ValidateIfIsTheSameUserOrAdmin validateIfIsTheSameUserOrAdmin, ValidateIfUserIsAdmin validateIfUserIsAdmin, ValidaIfCategoryBelongToUser validaIfCategoryBelongToUser, ValidateIfPasswordHasEmptyProperties validateIfPasswordHasEmptyProperties) {
        this.passwordRepository = passwordRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.validateIfIsTheSameUserOrAdmin = validateIfIsTheSameUserOrAdmin;
        this.validateIfUserIsAdmin = validateIfUserIsAdmin;
        this.validaIfCategoryBelongToUser = validaIfCategoryBelongToUser;
        this.validateIfPasswordHasEmptyProperties = validateIfPasswordHasEmptyProperties;
    }

    public List<Password> getAllPasswordsByUserId(Principal principal, Long userId) throws Exception {

        validateIfIsTheSameUserOrAdmin.validate(principal, userId);

        return passwordRepository.findAllByUserId(userId);
    }

    public Password createPassword(Principal principal, Password password) throws Exception{

        validateIfIsTheSameUserOrAdmin.validate(principal, password.getUserId());
        validateIfPasswordHasEmptyProperties.validate(password);

        Category category = categoryRepository.findById(password.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category not found"));

        validaIfCategoryBelongToUser.validate(category, password.getUserId());

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

        validateIfIsTheSameUserOrAdmin.validate(principal, passwordToUpdate.getUserId());
        validaIfCategoryBelongToUser.validate(category, passwordToUpdate.getUserId());

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

        validateIfPasswordHasEmptyProperties.validate(passwordToUpdate);

        return passwordToUpdate;
    }

    public void deletePassword(Principal principal, Long passwordId) throws Exception{

        Password passwordToDelete =
                passwordRepository.findById(passwordId).orElseThrow(() -> new EntityNotFoundException("Password does not exist"));

        validateIfIsTheSameUserOrAdmin.validate(principal, passwordToDelete.getUserId());



        passwordRepository.deleteById(passwordId);
    }
}
