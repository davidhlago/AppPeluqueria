package com.peluqueria.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String curso; // AÑADIDO

    @Column(length = 50)
    private String turno; // AÑADIDO

    @ManyToMany(mappedBy = "grupos")
    private List<Usuario> usuarios = new ArrayList<>();

    // --- Constructores ---
    public Grupo() {}
    public Grupo(String curso, String turno) {
        this.curso = curso;
        this.turno = turno;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}