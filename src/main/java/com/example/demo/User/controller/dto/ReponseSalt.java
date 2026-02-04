package com.example.demo.User.controller.dto;

public class ReponseSalt
{
    private String salt;

    public ReponseSalt(String salt)
    {
        this.salt = salt;
    }

    public String getSalt()
    {
        return salt;
    }
}