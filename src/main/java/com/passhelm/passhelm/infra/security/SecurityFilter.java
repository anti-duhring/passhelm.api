package com.passhelm.passhelm.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenJWT = getToken(request);

        System.out.println(tokenJWT);

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if(authHeader!= null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        throw new RuntimeException("Token not found");
    }
}
