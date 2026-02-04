package com.example.demo.User.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
public class User {

    private Long id;

    private String name;

    private String email;

    
    private String pass;

    private String salt;

    private boolean isConnected = false;

    
    private String jwtToken;

    private Long tokenExpiry;

    private String resetCode;

    private Long resetCodeExpiry;

    // JPA requires a no-args constructor
    public User() {}

    public User(Long id ,String email, String name, String pass, String salt) {
        this.id = id ;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.salt = salt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass(){
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSalt(){
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Long getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Long tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public Long getResetCodeExpiry() {
        return resetCodeExpiry;
    }

    public void setResetCodeExpiry(Long resetCodeExpiry) {
        this.resetCodeExpiry = resetCodeExpiry;
    }

}