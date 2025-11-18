package com.peluqueria.project.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String rol;

    // Relación Many-to-Many con Grupo, usando List en lugar de Set
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "grupo_id")
    )
    private List<com.peluqueria.project.modelo.Grupo> grupos = new ArrayList<>();

    // --- Constructores ---
    public Usuario() {}
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // [Otros Getters y Setters...]
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<com.peluqueria.project.modelo.Grupo> getGrupos() { return grupos; }
    public void setGrupos(List<com.peluqueria.project.modelo.Grupo> grupos) { this.grupos = grupos; }
    //...
}