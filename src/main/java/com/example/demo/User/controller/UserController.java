package com.example.demo.User.controller;

import com.example.demo.User.controller.dto.ResetPasswordRequest;
import com.example.demo.User.controller.dto.ForgotPasswordRequest;
import com.example.demo.User.controller.dto.LoginResponse;
import com.example.demo.User.controller.dto.ReponseSalt;
import com.example.demo.User.controller.dto.SaltRequest;
import com.example.demo.User.controller.dto.LoginRequest;
import com.example.demo.User.model.User;
import com.example.demo.User.controller.dto.CreateUserRequest;
import com.example.demo.User.service.UserService; 
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
     @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    // REGISTER
    @PostMapping("/register")
    public void register(@Valid @RequestBody CreateUserRequest request) { 
        String mail = request.getEmail();

        Optional<User> user = userService.getUserByEmail(mail);

        String pwd = request.getPassword();

        if (user.isPresent()) {
            user.get().setPass(userService.hash(pwd+userService.getPeper()));
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            userService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token invalide"));
        }
    }

    // MOT DE PASSE OUBLIÉ - Demande le code
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            String code = userService.forgotPassword(request);
            return ResponseEntity.ok(Map.of(
                "message", "Code envoyé (voir console serveur)",
                "code", code  // Pour les tests uniquement !
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // RÉINITIALISER LE MOT DE PASSE avec le code
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // VALIDER LE TOKEN
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            boolean valid = userService.validateToken(token);
            return ResponseEntity.ok(Map.of("valid", valid));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("valid", false));
        }
    }

    @PostMapping("/user/sel")
    public ReponseSalt postSendSalt(@Valid @RequestBody SaltRequest request) {
        String mail = request.getMail();

        User user = userService.addNewUser(request.getName() , mail, "", userService.generateSalt());

        return new ReponseSalt(user.getSalt());
    }
}
