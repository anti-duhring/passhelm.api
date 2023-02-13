package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final String ADMIN_ROLE = "ROLE_ADMIN";

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-user-role/{userId}")
    @Transactional
    public ResponseEntity updateUserRole(
            Principal principal,
            @PathVariable("userId") Long userId,
            @RequestBody List<String> roles
    ) {

        Boolean isAdmin = userService.isPrincipalAdmin(principal);

        if(!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        User userUpdated = userService.updateUserRoles(userId, roles);

        return ResponseEntity.ok(userUpdated);
    }
}
