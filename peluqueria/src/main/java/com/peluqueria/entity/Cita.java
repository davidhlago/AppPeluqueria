package com.peluqueria.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_grupo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "id_horario_semana", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HorarioSemanal horarioSemanal;



    public Cita() {}

    // Getters y Setters
    public Long getIdCita() { return idCita; }
    public void setIdCita(Long idCita) { this.idCita = idCita; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public HorarioSemanal getHorarioSemanal() { return horarioSemanal; }
    public void setHorarioSemanal(HorarioSemanal horarioSemanal) { this.horarioSemanal = horarioSemanal; }

    // Método auxiliar si quieres facilitar el acceso al servicio en el JSON de respuesta
    public Servicio getServicio() {
        return horarioSemanal != null ? horarioSemanal.getServicio() : null;
    }
}