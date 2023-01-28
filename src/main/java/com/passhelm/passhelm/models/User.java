package com.passhelm.passhelm.models;

public class User {

    private Long id;
    private String username;
    private String name;
    private String email;
    private Boolean isEmailVerified;
    private String password;

    public User(String username, String name, String email, Boolean isEmailVerified, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.isEmailVerified = isEmailVerified;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
