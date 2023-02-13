package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.records.UserResponse;
import com.passhelm.passhelm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
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
    public ResponseEntity<List<User>> getAllUsers(Principal principal) throws AccessDeniedException {

        List<User> users = userService.getAllUsers(principal);

        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> addUser(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {

        User newUser = userService.addUser(user);

        URI uri = uriComponentsBuilder.path("/user/{id}").buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(new UserResponse(newUser));
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity deleteUser(Principal principal, @PathVariable("id") Long id) throws AccessDeniedException {
        userService.deleteUser(id, principal);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/user/{id}")
    public ResponseEntity<UserResponse> updateUser(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestBody User user
    ) throws AccessDeniedException {

        User updatedUser = userService.updateUser(id, user, principal);

        return ResponseEntity.ok(new UserResponse(updatedUser));
    }

}
