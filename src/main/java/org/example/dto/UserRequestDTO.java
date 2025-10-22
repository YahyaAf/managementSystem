package org.example.dto;

import org.example.model.enums.Role;

public class UserRequestDTO {

    private String nom;
    private String email;
    private String motDePasse;
    private Role role;
    private Boolean actif;

    // Constructeurs
    public UserRequestDTO() {
    }

    public UserRequestDTO(String nom, String email, String motDePasse, Role role, Boolean actif) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.actif = actif;
    }

    // Getters & Setters
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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
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

    @Override
    public String toString() {
        return "UserRequestDTO{" +
                "nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", actif=" + actif +
                '}';
    }
}