package com.example.demo.User.controller.dto;

public class SaltRequest {
    
    private String name;
    private String mail;

    public SaltRequest(String mail)
    {
        this.mail = mail;
    }

    public String getName()
    {
        return name;
    }

    public String getMail()
    {
        return mail;
    }
}