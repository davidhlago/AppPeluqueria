package com.peluqueria.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("GRUPO")  // valor que se guarda en la columna tipo_usuario
public class Grupo extends Usuario {

    private String curso;
    private String turno;

    public Grupo() {}

    public Grupo(String nombre, String apellidos, String email, String password,
                 String curso, String turno) {
        super(nombre, apellidos, email, password);
        this.curso = curso;
        this.turno = turno;
    }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}
