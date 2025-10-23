package org.example.dto;

public class LoginRequestDTO {
    private String email;
    private String motDePasse;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}