package com.example.demo.User.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    
    private String pass;

    private String salt;

    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'USER'")
    private String role = "USER"; // "USER" ou "ADMIN"

    private boolean isConnected = false;

    
    private String jwtToken;

    private Long tokenExpiry;

    private String resetCode;

    private Long resetCodeExpiry;

    // JPA requires a no-args constructor
    public User() {}

    // User user = new User(id, name, mail, pswd, salt);
    public User(Long id ,String name, String email, String pass, String salt) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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