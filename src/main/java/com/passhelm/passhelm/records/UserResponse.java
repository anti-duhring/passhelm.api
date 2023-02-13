package com.passhelm.passhelm.records;

import com.passhelm.passhelm.models.User;

public record UserResponse(Long id, String username, String email, String name) {

    public UserResponse(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getName());
    }


}
