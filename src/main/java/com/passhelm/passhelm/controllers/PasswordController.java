package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.models.Password;
import com.passhelm.passhelm.records.PasswordResponse;
import com.passhelm.passhelm.service.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
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
    public ResponseEntity<List<Password>> getAllPasswordsByUserId(Principal principal, @Param("userId") Long userId) throws Exception {
        List<Password> passwords = passwordService.getAllPasswordsByUserId(principal, userId);
        return ResponseEntity.ok(passwords);
    }

    @PostMapping("/password")
    public ResponseEntity createPassword(Principal principal, @RequestBody Password password,
                                         UriComponentsBuilder uriBuilder) throws Exception {
        Password passwordCreated = passwordService.createPassword(principal, password);

        URI uri = uriBuilder.path("/password/{id}").buildAndExpand(passwordCreated.getId()).toUri();

        return ResponseEntity.created(uri).body(new PasswordResponse(passwordCreated));
    }

    @PutMapping("/password/{passwordId}")
    public ResponseEntity<PasswordResponse> updatePassword(Principal principal,
                                                           @PathVariable("passwordId") Long passwordId,
                                                           @RequestBody Password password) throws Exception{
        System.out.println(passwordId);
        Password passwordUpdated = passwordService.updatePassword(principal, passwordId, password);

        return ResponseEntity.ok(new PasswordResponse(passwordUpdated));
    }

    @DeleteMapping("/password/{passwordId}")
    public ResponseEntity deletePassword(Principal principal, @PathVariable("passwordId") Long passwordId) throws Exception {
        passwordService.deletePassword(principal, passwordId);

        return ResponseEntity.noContent().build();
    }
}
