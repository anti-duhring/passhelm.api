package com.passhelm.passhelm.service;

import com.passhelm.passhelm.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public List<User> getAllUsers() {

        return List.of(
                new User(
                        "mateusvnlima",
                        "Mateus Vinicius",
                        "mateusvnlima@gmail.com",
                        true,
                        "123456"
                )
        );
    }
}
