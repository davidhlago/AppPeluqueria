package com.peluqueria.security.service;

import com.peluqueria.entity.BloqueoHorario;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ServicioBloqueoHorario {
    BloqueoHorario crearBloqueo(BloqueoHorario bloqueo);

    List<BloqueoHorario> listarTodos();

    List<BloqueoHorario> listarPorFecha(LocalDate fecha);

    List<BloqueoHorario> listarPorGrupo(Long idGrupo);

    List<BloqueoHorario> listarPorServicio(Long idServicio);

    List<BloqueoHorario> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    boolean existeConflicto(LocalDate fecha, Long idGrupo, Long idServicio, LocalTime horaInicio, LocalTime horaFin);

    void eliminarBloqueo(Long id);

    BloqueoHorario actualizarBloqueo(Long id, BloqueoHorario bloqueoDetails);

    BloqueoHorario obtenerPorId(Long id);

    List<BloqueoHorario> crearBloqueoRango(BloqueoHorario base, LocalDate fechaFin);
}
