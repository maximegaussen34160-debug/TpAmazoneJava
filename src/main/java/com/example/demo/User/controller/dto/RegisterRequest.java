package com.example.demo.User.controller.dto;

public class RegisterRequest {
    private String email;
    private String name;
    private String pass;
    private String salt;

    public RegisterRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
}