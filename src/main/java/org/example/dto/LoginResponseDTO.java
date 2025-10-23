package org.example.dto;

public class LoginResponseDTO {
    private Long userId;
    private String email;
    private String nom;
    private String role;
    private Boolean actif;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Long userId, String email, String nom, String role, Boolean actif) {
        this.userId = userId;
        this.email = email;
        this.nom = nom;
        this.role = role;
        this.actif = actif;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getRole() {
        return role;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}