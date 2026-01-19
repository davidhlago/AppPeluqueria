package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map; // <--- IMPORTANTE

public interface ServicioCita {
    Cita crearCita(Cita cita);
    Cita modificarCita(Long id, Cita cita);
    Cita gestionarEstadoCita(Long id, int opcion);
    void cancelarCita(Long id);
    List<Cita> obtenerTodas();
    List<Cita> obtenerPorGrupo(Long id);
    List<Cita> obtenerPorCliente(Long id);
    List<Cita> obtenerPorAlumno(Long id);
    Cita obtenerPorId(Long id);

    // Tus métodos existentes
    List<LocalTime> obtenerHuecosDisponibles(LocalDate f, Long s, Long g);

    // EL NUEVO MÉTODO PARA FLUTTER
    List<Map<String, Object>> obtenerHuecosPorServicioYFecha(Long idServicio, LocalDate fecha);
}