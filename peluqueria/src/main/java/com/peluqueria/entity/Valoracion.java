package com.peluqueria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "valoracion")
public class Valoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracion")
    private Long idValoracion;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @Column(name = "trato_personal", nullable = false)
    private double tratoPersonal; // 1. Trato personal

    @Column(name = "desarrollo_servicio", nullable = false)
    private double desarrolloServicio; // 2. Desarrollo del servicio

    @Column(name = "claridad_comunicacion", nullable = false)
    private double claridadComunicacion; // 3. Claridad en la comunicacion

    @Column(name = "limpieza_organizacion", nullable = false)
    private double limpiezaOrganizacion; // 4. Limpieza y organizacion del entorno

    @Column(name = "general", nullable = false)
    private double general; // 5. General (antes puntuacion)

    @Column(name = "fecha_valoracion")
    private LocalDateTime fechaValoracion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Lob
    @Column(name = "imagen_base64", columnDefinition = "LONGTEXT")
    private String imagenBase64; // foto de antes/después, en base64

    public Valoracion() {
    }

    public Long getIdValoracion() {
        return idValoracion;
    }

    public void setIdValoracion(Long idValoracion) {
        this.idValoracion = idValoracion;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public double getTratoPersonal() {
        return tratoPersonal;
    }

    public void setTratoPersonal(double tratoPersonal) {
        this.tratoPersonal = tratoPersonal;
    }

    public double getDesarrolloServicio() {
        return desarrolloServicio;
    }

    public void setDesarrolloServicio(double desarrolloServicio) {
        this.desarrolloServicio = desarrolloServicio;
    }

    public double getClaridadComunicacion() {
        return claridadComunicacion;
    }

    public void setClaridadComunicacion(double claridadComunicacion) {
        this.claridadComunicacion = claridadComunicacion;
    }

    public double getLimpiezaOrganizacion() {
        return limpiezaOrganizacion;
    }

    public void setLimpiezaOrganizacion(double limpiezaOrganizacion) {
        this.limpiezaOrganizacion = limpiezaOrganizacion;
    }

    public double getGeneral() {
        return general;
    }

    public void setGeneral(double general) {
        this.general = general;
    }

    public LocalDateTime getFechaValoracion() {
        return fechaValoracion;
    }

    public void setFechaValoracion(LocalDateTime fechaValoracion) {
        this.fechaValoracion = fechaValoracion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }
}
