package com.peluqueria.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupo")
public class Grupo extends Usuario {

    @Column(length = 100)
    private String curso;

    @Column(length = 50)
    private String turno;

    @ManyToMany
    @JoinTable(
            name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    // --- Constructor vacío (necesario para JPA) ---
    public Grupo() {}

    // --- Constructor solo con atributos propios ---
    public Grupo(String curso, String turno) {
        this.curso = curso;
        this.turno = turno;
    }

    // --- Constructor completo con atributos heredados + propios ---
    public Grupo(String nombre, String apellidos, String email, String password,
                 String curso, String turno) {
        super(nombre, apellidos, email, password); // inicializa atributos de Usuario
        this.curso = curso;
        this.turno = turno;
    }

    // --- Getters y Setters ---
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}
