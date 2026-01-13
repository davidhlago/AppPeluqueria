package com.peluqueria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Transient;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_servicio_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoServicio tipoServicio;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion_bloques", nullable = false)
    private int duracionBloques;

    @Column(nullable = false)
    private double precio;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imagenBase64;

    public Servicio() {
    }

    public Servicio(Long idServicio, TipoServicio tipoServicio, String nombre, String descripcion, int duracionBloques, double precio, String imagenBase64) {
        this.idServicio = idServicio;
        this.tipoServicio = tipoServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionBloques = duracionBloques;
        this.precio = precio;
        this.imagenBase64 = imagenBase64;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getDuracionBloques() { return duracionBloques; }
    public void setDuracionBloques(int duracionBloques) { this.duracionBloques = duracionBloques; }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionBloques() {
        return duracionBloques;
    }

    public void setDuracionBloques(int duracionBloques) {
        this.duracionBloques = duracionBloques;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "idServicio=" + idServicio +
                ", tipoServicio=" + tipoServicio +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracionBloques=" + duracionBloques +
                ", precio=" + precio +
                ", tipoServicioId=" + tipoServicioId +
                '}';
    }
}
