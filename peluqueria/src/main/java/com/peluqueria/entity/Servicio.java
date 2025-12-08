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

    // ================================
    // Campo auxiliar para recibir solo el ID
    // ================================
    @Transient
    private Long tipoServicioId;

    public Servicio() {}

    public Servicio(Long idServicio, TipoServicio tipoServicio, String nombre,
                    String descripcion, int duracionBloques, double precio) {
        this.idServicio = idServicio;
        this.tipoServicio = tipoServicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionBloques = duracionBloques;
        this.precio = precio;
    }

    // Getters y setters
    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }

    public TipoServicio getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(TipoServicio tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getDuracionBloques() { return duracionBloques; }
    public void setDuracionBloques(int duracionBloques) { this.duracionBloques = duracionBloques; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    // Getter/Setter del campo auxiliar
    public Long getTipoServicioId() { return tipoServicioId; }
    public void setTipoServicioId(Long tipoServicioId) { this.tipoServicioId = tipoServicioId; }

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
