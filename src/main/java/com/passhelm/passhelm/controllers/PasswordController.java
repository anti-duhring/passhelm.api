package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/password")
    public List<Password> getAllPasswordsByUserId(@Param("userId") Long userId) {
        return passwordService.getAllPasswordsByUserId(userId);
    }
}
