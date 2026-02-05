package com.example.demo.User.controller;

import com.example.demo.User.controller.dto.*;
import com.example.demo.User.model.User;
import com.example.demo.User.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/salt")
    public ResponseEntity<SaltResponse> getSalt(@RequestParam(required = false) String email) {
        String salt;
        if (email != null && !email.isEmpty()) {
            salt = userService.getSaltByEmail(email);
        } else {
            salt = userService.generateNewSalt();
        }
        return ResponseEntity.ok(new SaltResponse(salt));
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        try {
            UserDTO user = userService.register(request);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String message = userService.forgotPassword(request);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<VerifyResetCodeResponse> verifyResetCode(@RequestBody VerifyResetCodeRequest request) {
        boolean valid = userService.verifyResetCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(new VerifyResetCodeResponse(valid));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }
}