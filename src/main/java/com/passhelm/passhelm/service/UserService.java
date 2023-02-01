package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.repository.UserRepository;
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
            throw new IllegalStateException("User does not exist");
        }

        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(
            Long id,
            String username,
            String email,
            String name
    ) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User does not exist"));

        if (name != null && name.length() > 0 && user.getName() != name) {
            user.setName(name);
        }
        if (email!= null && email.length() > 0 && user.getEmail()!= email) {
            Optional<User> userByEmail = userRepository.findByEmail(email);

            if(userByEmail.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            user.setEmail(email);
        }
        if(username!= null && username.length() > 0 && user.getUsername()!= username) {
            Optional<User> userByUsername = userRepository.findByUsername(username);

            if(userByUsername.isPresent()) {
                throw new IllegalStateException("Username taken");
            }

            user.setUsername(username);
        }

        return user;
    }
}
