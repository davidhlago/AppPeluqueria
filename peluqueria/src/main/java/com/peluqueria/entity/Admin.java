package com.peluqueria.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN") // valor que se guarda en la columna tipo_usuario
public class Admin extends Usuario {

    private String especialidad;

    // --- Constructores ---
    public Admin() {}

    public Admin(String nombre, String apellidos, String email, String password, String rol, String especialidad) {
        super(nombre, apellidos, email, password, rol);
        this.especialidad = especialidad;
    }

    // --- Getters y Setters ---
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
