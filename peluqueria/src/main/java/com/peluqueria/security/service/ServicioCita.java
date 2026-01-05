package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ServicioCita {
    Cita crearCita(Cita cita);
    Cita modificarCita(Long id, Cita citaDetalles); // Nuevo
    List<Cita> obtenerTodas();
    List<Cita> obtenerPorCliente(Long clienteId);
    List<Cita> obtenerPorGrupo(Long grupoId);
    List<Cita> obtenerPorAlumno(Long alumnoId);
    Cita obtenerPorId(Long id);
    void cancelarCita(Long id);
    List<LocalTime> obtenerHuecosDisponibles(LocalDate fecha, Long servicioId, Long grupoId);
}