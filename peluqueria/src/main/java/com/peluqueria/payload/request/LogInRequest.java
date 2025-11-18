package com.peluqueria.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LogInRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // --- Getters y Setters ---
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}