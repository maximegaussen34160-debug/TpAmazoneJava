package com.example.demo.User.service;

import com.example.demo.User.controller.dto.*;
import com.example.demo.User.model.User;
import com.example.demo.User.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordHasher passwordHasher;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordHasher = passwordHasher;
    }

    // GET /salt - Génère un nouveau salt (inscription)
    public String generateNewSalt() {
        return passwordHasher.generateSalt();
    }

    // GET /salt?email=... - Récupère le salt (login) - ANTI-ORACLE
    public String getSaltByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getSalt();
        } else {
            return passwordHasher.hash(email + "fake_salt_secret").substring(0, 24);
        }
    }

    // POST /user - Inscription
    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        String finalHash = passwordHasher.addPepperToHash(request.getPass());
        User user = new User(null, request.getName(), request.getEmail(), finalHash, request.getSalt());
        User saved = userRepository.save(user);

        return new UserDTO(saved.getId(), saved.getName(), saved.getEmail());
    }

    // POST /login - Connexion avec délai anti-brute-force
    public LoginResponse login(LoginRequest request) {
        try {
            Thread.sleep(1000); // Délai anti-brute-force
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        String finalHash = passwordHasher.addPepperToHash(request.getPassword());

        if (!user.getPass().equals(finalHash)) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(user.getEmail(),  user.getId());
        long expiresIn = 3600000;

        user.setJwtToken(token);
        user.setTokenExpiry(System.currentTimeMillis() + expiresIn);
        user.setConnected(true);
        userRepository.save(user);

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getName(), user.getRole(), expiresIn);
    }

    // POST /forgot-password
    public String forgotPassword(ForgotPasswordRequest request) {
        String message = "Si votre email existe, un code a été envoyé";
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String code = generateCode();
            user.setResetCode(code);
            user.setResetCodeExpiry(System.currentTimeMillis() + 300000);
            userRepository.save(user);
            System.out.println("Code pour " + request.getEmail() + " : " + code);
        }
        return message;
    }

    // POST /verify-reset-code
    public boolean verifyResetCode(String email, String code) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (user.getResetCode() == null || user.getResetCodeExpiry() == null) return false;
        if (System.currentTimeMillis() > user.getResetCodeExpiry()) return false;

        return user.getResetCode().equals(code);
    }

    // POST /reset-password
    public void resetPassword(ResetPasswordRequest request) {
        if (!verifyResetCode(request.getEmail(), request.getCode())) {
            throw new IllegalArgumentException("Code invalide ou expiré");
        }

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        String finalHash = passwordHasher.addPepperToHash(request.getNewPassword());
        user.setPass(finalHash);
        user.setSalt(request.getSalt());
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);
    }

    private String generateCode() {
        return String.valueOf(100000 + secureRandom.nextInt(900000));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}