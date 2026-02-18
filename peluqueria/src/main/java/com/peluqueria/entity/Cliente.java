package com.peluqueria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    private String telefono;
    private String direccion;
    private String observacion;
    private String alergenos;

    // columnDefinition = "LONGTEXT" es recomendado para MySQL para soportar cadenas muy largas.
    @Lob
    @Column(name = "imagen_base64", columnDefinition = "LONGTEXT")
    @com.fasterxml.jackson.annotation.JsonProperty("imagen_base64")
    private String imagenBase64;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Grupo grupo;

    // -----------------------------------------------------------
    // RELACIÓN: FAVORITOS
    // -----------------------------------------------------------
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favoritos_cliente_servicio",
            joinColumns = @JoinColumn(name = "id_cliente"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "clientesFans"})
    private Set<Servicio> serviciosFavoritos = new HashSet<>();

    public Cliente() {}

    // --- Getters y Setters existentes ---

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

    public Set<Servicio> getServiciosFavoritos() { return serviciosFavoritos; }
    public void setServiciosFavoritos(Set<Servicio> serviciosFavoritos) { this.serviciosFavoritos = serviciosFavoritos; }

    // ✅ Getters y Setters NUEVOS para Imagen Base64
    public String getImagenBase64() { return imagenBase64; }
    public void setImagenBase64(String imagenBase64) { this.imagenBase64 = imagenBase64; }
}