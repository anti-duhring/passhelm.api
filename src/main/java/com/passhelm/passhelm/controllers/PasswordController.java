package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.records.PasswordResponse;
import com.passhelm.passhelm.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<List<Password>> getAllPasswordsByUserId(@Param("userId") Long userId) {
        List<Password> passwords = passwordService.getAllPasswordsByUserId(userId);
        return ResponseEntity.ok(passwords);
    }

    @PostMapping("/password")
    public ResponseEntity createPassword(@RequestBody Password password, UriComponentsBuilder uriBuilder) {
        Password passwordCreated = passwordService.createPassword(password);

        URI uri = uriBuilder.path("/password/{id}").buildAndExpand(passwordCreated.getId()).toUri();

        return ResponseEntity.created(uri).body(new PasswordResponse(passwordCreated));
    }

    @PutMapping("/password/{passwordId}")
    public ResponseEntity<PasswordResponse> updatePassword(@PathVariable("passwordId") Long passwordId,
                                                           @RequestBody Password password) {
        Password passwordUpdated = passwordService.updatePassword(passwordId, password);

        return ResponseEntity.ok(new PasswordResponse(passwordUpdated));
    }

    @DeleteMapping("/password/{passwordId}")
    public ResponseEntity deletePassword(@PathVariable("passwordId") Long passwordId) {
        passwordService.deletePassword(passwordId);

        return ResponseEntity.noContent().build();
    }
}
