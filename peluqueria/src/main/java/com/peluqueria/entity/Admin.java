package com.peluqueria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends Usuario {

    @Column(length = 100)
    private String especialidad;

    // Constructor vacío (necesario para JPA)
    public Admin() {}

    // Constructor completo con super
    public Admin(String nombre, String apellidos, String email, String password, String especialidad) {
        super(nombre, apellidos, email, password); // inicializa atributos heredados
        this.especialidad = especialidad;          // inicializa atributo propio
    }

    // Getters y Setters
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
