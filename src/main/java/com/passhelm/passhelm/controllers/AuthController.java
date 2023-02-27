package com.passhelm.passhelm.controllers;

import com.passhelm.passhelm.infra.security.AuthData;
import com.passhelm.passhelm.infra.security.JWTTokenData;
import com.passhelm.passhelm.infra.security.TokenService;
import com.passhelm.passhelm.models.User;
import com.passhelm.passhelm.records.LoginResponse;
import com.passhelm.passhelm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/login")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthData authData) throws Exception {


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authData.login(),
                authData.password());
        Authentication authenticate = manager.authenticate(authenticationToken);

        String tokenJWT = tokenService.generateToken((User) authenticate.getPrincipal());

        return ResponseEntity.ok(new LoginResponse((User) authenticate.getPrincipal(), tokenJWT));
    }

}
