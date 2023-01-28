package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user")
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
