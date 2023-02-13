package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(Principal principal) throws AccessDeniedException {

        Boolean isAdmin = this.isPrincipalAdmin(principal);
        if(!isAdmin) {
            throw new AccessDeniedException("Access denied");

        }
        return userRepository.findAll();
    }


    public User addUser(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        user.setAuthorities(List.of("ROLE_USER"));

        if(userByUsername.isPresent()) {
            throw new IllegalStateException("Username taken");
        }
        if(userByEmail.isPresent()) {
            throw new IllegalStateException("Email taken");
        }

        User newUser = userRepository.save(user);

        return newUser;

    }

    public void deleteUser(Long id, Principal principal) throws AccessDeniedException {

        Boolean userExists = userRepository.existsById(id);
        Boolean isAdmin = this.isPrincipalAdmin(principal);
        if(!isAdmin) {
            throw new AccessDeniedException("Access denied");

        }
        if(!userExists) {
            throw new EntityNotFoundException("User does not exist");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(
            Long id,
            User user,
            Principal principal
    ) throws AccessDeniedException {

        Boolean isAdmin = this.isPrincipalAdmin(principal);
        Long principalId = userRepository.findByUsername(principal.getName()).get().getId();
        if(!isAdmin && principalId!= id) {
            throw new AccessDeniedException("Access denied");

        }

        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User does not " +
                "exist"));

        if (user.getName() != null && user.getName().length() > 0 && userToUpdate.getName() != user.getName()) {
            userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null && user.getEmail().length() > 0 && userToUpdate.getEmail()!= user.getEmail()) {
            Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

            if(userByEmail.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            userToUpdate.setEmail(user.getEmail());
        }
        if(user.getUsername() != null && user.getUsername().length() > 0 && userToUpdate.getUsername()!= user.getUsername()) {
            Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());

            if(userByUsername.isPresent()) {
                throw new IllegalStateException("Username taken");
            }

            userToUpdate.setUsername(user.getUsername());
        }

        return userToUpdate;
    }

    public User getUser(Long id) {

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()) {
            throw new EntityNotFoundException("User does not exist");
        }


        return user.get();
    }

    public User updateUserRoles(Long id, List<String> roles) {
        User userToUpdate =
                userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not " + "exist"));

        userToUpdate.setAuthorities(roles);
        User userUpdated = userRepository.save(userToUpdate);

        return userUpdated;
    }

    public Boolean isPrincipalAdmin(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new EntityNotFoundException(
                "User does not " + "exist"));

        return user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
