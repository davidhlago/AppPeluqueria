package com.peluqueria.security.service;

import com.peluqueria.entity.BloqueoHorario;
import com.peluqueria.entity.Grupo;
import com.peluqueria.entity.Servicio;
import com.peluqueria.repository.BloqueoHorarioRepository;
import com.peluqueria.repository.GrupoRepository;
import com.peluqueria.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ServicioBloqueoHorarioImpl implements ServicioBloqueoHorario {

    @Autowired
    private BloqueoHorarioRepository bloqueoRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    @Transactional
    public BloqueoHorario crearBloqueo(BloqueoHorario bloqueo) {
        // Validaciones
        if (bloqueo.getFecha() == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }

        if (!bloqueo.isTodoElDia()) {
            if (bloqueo.getHoraInicio() == null || bloqueo.getHoraFin() == null) {
                throw new RuntimeException("Debes indicar hora inicio y fin si no es todo el día");
            }
            if (bloqueo.getHoraInicio().isAfter(bloqueo.getHoraFin())) {
                throw new RuntimeException("La hora de inicio no puede ser posterior a la hora fin");
            }
        } else {
            // Si es todo el día, limpiamos las horas
            bloqueo.setHoraInicio(null);
            bloqueo.setHoraFin(null);
        }

        // Asignar entidades reales (Evita TransientObjectException)
        if (bloqueo.getGrupo() != null && bloqueo.getGrupo().getId() != null) {
            Grupo g = grupoRepository.findById(bloqueo.getGrupo().getId())
                    .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            bloqueo.setGrupo(g);
        } else {
            bloqueo.setGrupo(null);
        }

        if (bloqueo.getServicio() != null && bloqueo.getServicio().getIdServicio() != null) {
            Servicio s = servicioRepository.findById(bloqueo.getServicio().getIdServicio())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            bloqueo.setServicio(s);
        } else {
            bloqueo.setServicio(null);
        }

        return bloqueoRepository.save(bloqueo);
    }

    @Override
    public List<BloqueoHorario> listarTodos() {
        return bloqueoRepository.findAll();
    }

    @Override
    public List<BloqueoHorario> listarPorFecha(LocalDate fecha) {
        return bloqueoRepository.findByFecha(fecha);
    }

    @Override
    public List<BloqueoHorario> listarPorGrupo(Long idGrupo) {
        return bloqueoRepository.findByGrupoId(idGrupo);
    }

    @Override
    public List<BloqueoHorario> listarPorServicio(Long idServicio) {
        return bloqueoRepository.findByServicioIdServicio(idServicio);
    }

    @Override
    public List<BloqueoHorario> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return bloqueoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Override
    public boolean existeConflicto(LocalDate fecha, Long idGrupo, Long idServicio,
            LocalTime horaInicio, LocalTime horaFin) {
        return bloqueoRepository.existeBloqueo(fecha, idGrupo, idServicio, horaInicio, horaFin);
    }

    @Override
    public void eliminarBloqueo(Long id) {
        if (!bloqueoRepository.existsById(id)) {
            throw new RuntimeException("Bloqueo no encontrado con ID: " + id);
        }
        bloqueoRepository.deleteById(id);
    }

    @Override
    public BloqueoHorario obtenerPorId(Long id) {
        return bloqueoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloqueo no encontrado con ID: " + id));
    }
}
