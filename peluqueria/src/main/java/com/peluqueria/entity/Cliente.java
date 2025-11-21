package com.peluqueria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("CLIENTE")  // valor que se guarda en la columna tipo_usuario
// NOTA: Se creará una tabla 'cliente' (o similar)
public class Cliente extends Usuario {

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String observacion;

    @Column(length = 255)
    private String alergenos;

    @Column(length = 255)
    private String direccion;

    public Cliente() {}

    public Cliente(String nombre, String apellidos, String email, String password,
                   String telefono, String observacion, String alergenos, String direccion) {
        super(nombre, apellidos, email, password);
        this.telefono = telefono;
        this.observacion = observacion;
        this.alergenos = alergenos;
        this.direccion = direccion;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlergenos() { return alergenos; }
    public void setAlergenos(String alergenos) { this.alergenos = alergenos; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}