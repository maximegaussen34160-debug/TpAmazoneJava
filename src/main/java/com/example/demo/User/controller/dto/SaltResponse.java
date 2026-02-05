package com.example.demo.User.controller.dto;

public class SaltResponse {
    private String salt;

    public SaltResponse() {}
    public SaltResponse(String salt) { this.salt = salt; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
}