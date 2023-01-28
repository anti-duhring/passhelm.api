package com.passhelm.passhelm.models;

public class Password {

    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String login;
    private String password;

    public Password(Long userId, Long categoryId, String title, String login, String password) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
