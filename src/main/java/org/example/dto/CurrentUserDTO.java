package org.example.dto;

public class CurrentUserDTO {
    private Long id;
    private String nom;
    private String email;
    private String role;
    private Boolean actif;

    public CurrentUserDTO() {
    }

    public CurrentUserDTO(Long id, String nom, String email, String role, Boolean actif) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
        this.actif = actif;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}