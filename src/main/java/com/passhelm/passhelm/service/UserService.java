package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    public User addUser(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        if(userByUsername.isPresent()) {
            throw new IllegalStateException("Username taken");
        }
        if(userByEmail.isPresent()) {
            throw new IllegalStateException("Email taken");
        }

        User newUser = userRepository.save(user);

        return newUser;

    }

    public void deleteUser(Long id) {

        Boolean userExists = userRepository.existsById(id);
        if(!userExists) {
            throw new EntityNotFoundException("User does not exist");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(
            Long id,
            User user
    ) {
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
}
