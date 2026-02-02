package com.peluqueria.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORTANTE
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet; // <--- NUEVO IMPORT
import java.util.Set;     // <--- NUEVO IMPORT

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @ManyToOne(fetch = FetchType.EAGER)
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

    // -----------------------------------------------------------
    // ✅ NUEVA RELACIÓN INVERSA (Para contar likes)
    // -----------------------------------------------------------
    @ManyToMany(mappedBy = "serviciosFavoritos", fetch = FetchType.LAZY)
    @JsonIgnore // ¡CRÍTICO! Esto evita que al pedir un servicio te traiga 1000 clientes
    private Set<Cliente> clientesFans = new HashSet<>();

    public Servicio() {}

    // -----------------------------------------------------------
    // ✅ MÉTODO EXTRA PARA EL FRONTEND
    // -----------------------------------------------------------
    // Este método devuelve el número de likes sin tener que enviar toda la lista de personas
    @Transient // No es un campo de la BD, se calcula al vuelo
    public int getNumeroLikes() {
        return clientesFans.size();
    }

    // Getters y Setters existentes
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

    // ✅ Getters y Setters NUEVOS
    public Set<Cliente> getClientesFans() { return clientesFans; }
    public void setClientesFans(Set<Cliente> clientesFans) { this.clientesFans = clientesFans; }
}