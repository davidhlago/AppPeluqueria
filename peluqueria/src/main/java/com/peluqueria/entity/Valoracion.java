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

    @Column(nullable = false)
    private int puntuacion; // 1..5

    @Column(name = "fecha_valoracion")
    private LocalDateTime fechaValoracion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Lob
    @Column(name = "imagen_base64", columnDefinition = "LONGTEXT")
    private String imagenBase64; // foto de antes/después, en base64

    public Valoracion() {}

    public Long getIdValoracion() { return idValoracion; }
    public void setIdValoracion(Long idValoracion) { this.idValoracion = idValoracion; }

    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public LocalDateTime getFechaValoracion() { return fechaValoracion; }
    public void setFechaValoracion(LocalDateTime fechaValoracion) { this.fechaValoracion = fechaValoracion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getImagenBase64() { return imagenBase64; }
    public void setImagenBase64(String imagenBase64) { this.imagenBase64 = imagenBase64; }
}
