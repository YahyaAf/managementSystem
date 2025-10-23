package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.dto.CurrentUserDTO;
import org.example.dto.LoginRequestDTO;
import org.example.dto.LoginResponseDTO;
import org.example.model.User;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequestDTO loginRequest,
            HttpSession session
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = authService.login(loginRequest);

            session.setAttribute("currentUser", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userRole", user.getRole().name());
            session.setAttribute("userNom", user.getNom());

            LoginResponseDTO loginResponse = new LoginResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getNom(),
                    user.getRole().name(),
                    user.getActif()
            );

            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("data", loginResponse);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = (Long) session.getAttribute("currentUser");

            if (userId == null) {
                throw new RuntimeException("Not authenticated. Please login.");
            }

            CurrentUserDTO currentUser = authService.getCurrentUserById(userId);

            response.put("status", "success");
            response.put("data", currentUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = (Long) session.getAttribute("currentUser");

            session.invalidate();

            if (userId != null) {
                authService.logout(userId);
            }

            response.put("status", "success");
            response.put("message", "Logout successful");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long userId = (Long) session.getAttribute("currentUser");
        String userEmail = (String) session.getAttribute("userEmail");
        String userRole = (String) session.getAttribute("userRole");
        String userNom = (String) session.getAttribute("userNom");

        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("authenticated", userId != null);
        sessionData.put("userId", userId);
        sessionData.put("userEmail", userEmail);
        sessionData.put("userRole", userRole);
        sessionData.put("userNom", userNom);
        sessionData.put("sessionId", session.getId());

        response.put("status", "success");
        response.put("data", sessionData);
        return ResponseEntity.ok(response);
    }
}