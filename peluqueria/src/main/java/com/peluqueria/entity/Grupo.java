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

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    // Relación Many-to-Many, usando List en lugar de Set
    @ManyToMany(mappedBy = "grupos")
    private List<com.peluqueria.entity.Usuario> usuarios = new ArrayList<>();

    // --- Constructores ---
    public Grupo() {}
    public Grupo(String nombre) { this.nombre = nombre; }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<com.peluqueria.entity.Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<com.peluqueria.entity.Usuario> usuarios) { this.usuarios = usuarios; }
}