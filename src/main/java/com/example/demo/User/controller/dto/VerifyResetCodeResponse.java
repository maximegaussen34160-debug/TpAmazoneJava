package com.example.demo.User.controller.dto;

public class VerifyResetCodeResponse {
    private boolean valid;

    public VerifyResetCodeResponse() {}
    public VerifyResetCodeResponse(boolean valid) { this.valid = valid; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
}