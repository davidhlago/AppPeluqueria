package com.peluqueria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio") // Coincide con tu DataPropertyName en C#
    private Long idServicio;

    @ManyToOne(fetch = FetchType.EAGER) // Carga inmediata para que no salga null en el JSON
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

    public Servicio() {}

    // Getters y Setters
    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getDuracionBloques() { return duracionBloques; }
    public void setDuracionBloques(int duracionBloques) { this.duracionBloques = duracionBloques; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }
    public String getImagenBase64() { return imagenBase64; }
    public void setImagenBase64(String imagenBase64) { this.imagenBase64 = imagenBase64; }
}