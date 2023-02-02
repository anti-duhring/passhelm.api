package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {

        return userService.addUser(user);
    }

    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping(path = "/user/{id}")
    public User updateUser(
            @PathVariable("id") Long id,
            @RequestBody User user
 /*           @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "name", required = false) String name*/
    ) {

        return userService.updateUser(id, user);
    }

}
