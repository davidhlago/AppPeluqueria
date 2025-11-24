package com.peluqueria.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Usuario {

    private String especialidad;


    public Admin() {}

    public Admin(String nombre, String apellidos, String email, String password, String rol, String especialidad) {
        super(nombre, apellidos, email, password, rol);
        this.especialidad = especialidad;
    }


    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}