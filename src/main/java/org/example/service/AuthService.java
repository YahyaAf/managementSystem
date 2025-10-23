package org.example.service;

import org.example.dto.CurrentUserDTO;
import org.example.dto.LoginRequestDTO;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User login(LoginRequestDTO loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (loginRequest.getMotDePasse() == null || loginRequest.getMotDePasse().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User account has been deleted");
        }

        if (!user.getActif()) {
            throw new RuntimeException("User account is inactive");
        }

        if (!passwordEncoder.matches(loginRequest.getMotDePasse(), user.getMotDePasse())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public CurrentUserDTO getCurrentUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getDeletedAt() != null) {
            throw new RuntimeException("User account has been deleted");
        }

        CurrentUserDTO dto = new CurrentUserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setActif(user.getActif());

        return dto;
    }

    public void logout(Long userId) {
        // Example:
        // AuditLog log = new AuditLog();
        // log.setUserId(userId);
        // log.setAction("LOGOUT");
        // log.setTimestamp(LocalDateTime.now());
        // auditLogRepository.save(log);
    }
}