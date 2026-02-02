package com.peluqueria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet; // <--- NUEVO IMPORT
import java.util.Set;     // <--- NUEVO IMPORT

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    private String telefono;
    private String direccion;
    private String observacion;
    private String alergenos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Grupo grupo;

    // -----------------------------------------------------------
    // ✅ NUEVA RELACIÓN: FAVORITOS
    // -----------------------------------------------------------
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favoritos_cliente_servicio",       // Nombre exacto de tu tabla SQL
            joinColumns = @JoinColumn(name = "id_cliente"), // Columna FK hacia Cliente
            inverseJoinColumns = @JoinColumn(name = "id_servicio") // Columna FK hacia Servicio
    )
    // Ignoramos la lista inversa en Servicio para evitar bucle infinito
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "clientesFans"})
    private Set<Servicio> serviciosFavoritos = new HashSet<>();

    public Cliente() {}

    // Getters y Setters existentes
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    // ✅ Getters y Setters NUEVOS para Favoritos
    public Set<Servicio> getServiciosFavoritos() { return serviciosFavoritos; }
    public void setServiciosFavoritos(Set<Servicio> serviciosFavoritos) { this.serviciosFavoritos = serviciosFavoritos; }
}