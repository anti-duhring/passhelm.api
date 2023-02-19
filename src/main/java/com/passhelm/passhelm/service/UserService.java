package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
import com.passhelm.passhelm.validators.user.ValidateIfIsTheSameUserOrAdmin;
import com.passhelm.passhelm.validators.user.ValidateIfUserIsAdmin;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private final ValidateIfUserIsAdmin validateIfUserIsAdmin;

    @Autowired
    private final ValidateIfIsTheSameUserOrAdmin validateIfIsTheSameUserOrAdmin;

    @Autowired
    public UserService(UserRepository userRepository, ValidateIfUserIsAdmin validateIfUserIsAdmin, ValidateIfIsTheSameUserOrAdmin validateIfIsTheSameUserOrAdmin) {
        this.userRepository = userRepository;
        this.validateIfUserIsAdmin = validateIfUserIsAdmin;
        this.validateIfIsTheSameUserOrAdmin = validateIfIsTheSameUserOrAdmin;
    }

    public List<User> getAllUsers(Principal principal) throws Exception {

        validateIfUserIsAdmin.validate(principal);

        System.out.println(principal.getName());

        return userRepository.findAll();
    }


    public User addUser(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        User newUser = new User(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                List.of("ROLE_USER")
        );

        if(userByUsername.isPresent()) {
            throw new IllegalStateException("Username taken");
        }
        if(userByEmail.isPresent()) {
            throw new IllegalStateException("Email taken");
        }

        User userPersisted = userRepository.save(newUser);

        return userPersisted;

    }

    public void deleteUser(Long id, Principal principal) throws Exception {

        validateIfUserIsAdmin.validate(principal);

        Boolean userExists = userRepository.existsById(id);
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
    ) throws Exception {

        validateIfIsTheSameUserOrAdmin.validate(principal, id);

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

    public User updateUserRoles(Principal principal, Long id, List<String> roles) throws Exception {

        validateIfUserIsAdmin.validate(principal);

        User userToUpdate =
                userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User does not " + "exist"));

        userToUpdate.setAuthorities(roles);
        User userUpdated = userRepository.save(userToUpdate);

        return userUpdated;
    }

}
