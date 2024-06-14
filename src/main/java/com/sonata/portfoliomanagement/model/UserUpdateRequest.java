package com.sonata.portfoliomanagement.model;

public class UserUpdateRequest {
    private String email;
    private String oldPassword;
    private String newPassword;

    // Getters and setters for email, oldPassword, and newPassword
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

