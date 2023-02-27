package com.passhelm.passhelm.records;

import com.passhelm.passhelm.models.User;

public record LoginResponse(Long id, String username, String email, String name, String token) {

    public LoginResponse(User user, String token) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getName(), token);
    }
}
