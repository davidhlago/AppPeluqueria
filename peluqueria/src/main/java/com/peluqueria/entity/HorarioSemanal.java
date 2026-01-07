package com.peluqueria.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalTime;

@Entity
@Table(name = "horario_semanal")
public class HorarioSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHorarioSemana;

    private String diasSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cupoMaximo;

    @ManyToOne
    @JoinColumn(name = "id_grupo")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // <--- IMPORTANTE
    private Servicio servicio;

    public HorarioSemanal() {}

    // Getters y Setters
    public Long getIdHorarioSemana() { return idHorarioSemana; }
    public void setIdHorarioSemana(Long idHorarioSemana) { this.idHorarioSemana = idHorarioSemana; }
    public String getDiasSemana() { return diasSemana; }
    public void setDiasSemana(String diasSemana) { this.diasSemana = diasSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
}