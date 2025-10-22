package org.example.dto;

import org.example.model.enums.Role;
import org.example.entity.User;

import java.time.LocalDateTime;

public class UserResponseDTO {

    private Long id;
    private String nom;
    private String email;
    private Role role;
    private Boolean actif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructeurs
    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String nom, String email, Role role, Boolean actif,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
        this.actif = actif;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Méthode static pour convertir Entity → DTO
    public static UserResponseDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getNom(),
                user.getEmail(),
                user.getRole(),
                user.getActif(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", actif=" + actif +
                '}';
    }
}