package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.records.UserResponse;
import com.passhelm.passhelm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);

        return ResponseEntity.ok(new UserResponse(user));
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> addUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {

        User newUser = userService.addUser(user);

        URI uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserResponse(newUser));
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/user/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("id") Long id,
            @RequestBody User user
    ) {

        User updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(new UserResponse(updatedUser));
    }

}
