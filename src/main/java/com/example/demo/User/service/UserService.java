package com.example.demo.User.service;

import com.example.demo.User.controller.dto.*;
import com.example.demo.User.model.User;
import com.example.demo.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SecureRandom secureRandom;
    private final String pepper;

    public UserService(UserRepository userRepository,
                      JwtService jwtService,
                      @Value("${app.security.password.pepper}") String pepper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.secureRandom = new SecureRandom();
        this.pepper = pepper;
    }

    public Optional<User> getUserByEmail(String mail) {
        return userRepository.findByEmail(mail); 
    }

    public List<User> getUsers() {
        return userRepository.findAlls();
    }

    public String getPeper(){
        return this.pepper;
    }
    // Hash simple avec SHA-256
    public String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de hashage", e);
        }
    }

    // Générer un salt aléatoire
    public String generateSalt() {
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    // Générer un code à 6 chiffres
    private String generateCode() {
        int code = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(code);
    }

    // REGISTER
    // public UserDTO register(CreateUserRequest request) {
    //     if (userRepository.existsByEmail(request.getEmail())) {
    //         throw new IllegalArgumentException("Email déjà utilisé");
    //     }

    //     String salt = generateSalt();
    //     String hashedPassword = hash(request.getPassword() + salt + pepper);

    //     User user = new User(request.getEmail(), request.getName(), hashedPassword, salt);
    //     User saved = userRepository.save(user);

    //     return new UserDTO(saved.getId(), saved.getName(), saved.getEmail());
    // }

    // LOGIN
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        String hashedInput = hash(request.getPassword() + user.getSalt() + pepper);
        if (!hashedInput.equals(user.getPass())) {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId());
        long expiryTime = System.currentTimeMillis() + jwtService.getExpirationMs();

        user.setConnected(true);
        user.setJwtToken(token);
        user.setTokenExpiry(expiryTime);
        userRepository.save(user);

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getName(), jwtService.getExpirationMs());
    }

    // LOGOUT
    public void logout(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        user.setConnected(false);
        user.setJwtToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }

    // MOT DE PASSE OUBLIÉ - Génère un code
    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Email non trouvé"));

        String code = generateCode();
        long expiryTime = System.currentTimeMillis() + (10 * 60 * 1000); // 10 minutes

        user.setResetCode(code);
        user.setResetCodeExpiry(expiryTime);
        userRepository.save(user);

        // Afficher le code dans la console (au lieu d'envoyer par email)
        System.out.println("═══════════════════════════════════════");
        System.out.println("CODE DE RÉINITIALISATION");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Code: " + code);
        System.out.println("Valide pendant 10 minutes");
        System.out.println("═══════════════════════════════════════");

        return code; // Retourné pour les tests
    }

    // RÉINITIALISER LE MOT DE PASSE avec le code
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findAll().stream()
            .filter(u -> request.getCode().equals(u.getResetCode()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Code invalide"));

        if (user.getResetCodeExpiry() == null || System.currentTimeMillis() > user.getResetCodeExpiry()) {
            throw new IllegalArgumentException("Code expiré");
        }

        String salt = generateSalt();
        String hashedPassword = hash(request.getNewPassword() + salt + pepper);

        user.setPass(hashedPassword);
        user.setSalt(salt);
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        userRepository.save(user);
    }

    public User addNewUser(String name ,String identifiant , String pswd ,String salt) {
        return userRepository.addNewUser(name, identifiant , pswd , salt);
    }

    // VALIDER LE TOKEN
    public boolean validateToken(String token) {
        try {
            String email = jwtService.extractEmail(token);
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null || !token.equals(user.getJwtToken())) {
                return false;
            }

            if (user.getTokenExpiry() == null || System.currentTimeMillis() > user.getTokenExpiry()) {
                user.setConnected(false);
                user.setJwtToken(null);
                user.setTokenExpiry(null);
                userRepository.save(user);
                return false;
            }

            return user.isConnected();
        } catch (Exception e) {
            return false;
        }

        
    }
}
