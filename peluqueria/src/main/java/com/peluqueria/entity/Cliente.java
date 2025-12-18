package com.peluqueria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

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

    public Cliente() {}

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
}