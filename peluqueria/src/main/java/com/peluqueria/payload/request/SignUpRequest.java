package com.peluqueria.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String nombre;

    @NotBlank
    @Size(min = 3, max = 150)
    private String apellidos;

    @NotBlank
    @Size(max = 150)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    private String rol;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}