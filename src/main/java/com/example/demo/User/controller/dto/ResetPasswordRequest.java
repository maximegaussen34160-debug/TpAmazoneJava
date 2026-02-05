package com.example.demo.User.controller.dto;

public class ResetPasswordRequest {
    private String email;
    private String code;
    private String newPassword;
    private String salt;

    public ResetPasswordRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
}