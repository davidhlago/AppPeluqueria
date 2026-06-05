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

    @Autowired
    private com.peluqueria.repository.CitaRepository citaRepository;

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

        // 1. Validar conflictos con otros bloqueos
        List<BloqueoHorario> conflictos = bloqueoRepository.findConflictos(
                bloqueo.getFecha(),
                bloqueo.getGrupo() != null ? bloqueo.getGrupo().getId() : null,
                bloqueo.getServicio() != null ? bloqueo.getServicio().getIdServicio() : null,
                bloqueo.getHoraInicio(),
                bloqueo.getHoraFin());

        if (!conflictos.isEmpty()) {
            BloqueoHorario existente = conflictos.get(0);
            throw new com.peluqueria.exception.BloqueoHorarioException(
                    "Ya existe un bloqueo para esa fecha/hora. Motivo: " + existente.getMotivo());
        }

        // 2. Cancelar Citas existentes automáticamente
        cancelarCitasConflictivas(bloqueo);

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

    /**
     * Cancela automáticamente las citas que entran en conflicto con un bloqueo.
     */
    private void cancelarCitasConflictivas(BloqueoHorario bloqueo) {
        List<com.peluqueria.entity.Cita> citasConflictivas = citaRepository.findCitasParaBloqueo(
                bloqueo.getFecha(),
                bloqueo.getGrupo() != null ? bloqueo.getGrupo().getId() : null,
                bloqueo.getServicio() != null ? bloqueo.getServicio().getIdServicio() : null,
                bloqueo.getHoraInicio(),
                bloqueo.getHoraFin());

        if (!citasConflictivas.isEmpty()) {
            for (com.peluqueria.entity.Cita cita : citasConflictivas) {
                cita.setEstado("CANCELADA");
                cita.setMotivoCancelacion(bloqueo.getMotivo());
                citaRepository.save(cita);
                System.out.println(
                        "Cita cancelada automáticamente ID: " + cita.getIdCita() + " por conflicto con bloqueo.");
            }
        }
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
        return !bloqueoRepository.findConflictos(fecha, idGrupo, idServicio, horaInicio, horaFin).isEmpty();
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

    @Override
    @Transactional
    public BloqueoHorario actualizarBloqueo(Long id, BloqueoHorario datosNuevos) {
        // 1. Buscar el bloqueo existente
        BloqueoHorario actual = bloqueoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el bloqueo con ID: " + id));

        // 2. Actualizar datos simples
        actual.setFecha(datosNuevos.getFecha());
        actual.setMotivo(datosNuevos.getMotivo());
        actual.setTodoElDia(datosNuevos.isTodoElDia());

        // 3. Validar y actualizar horas
        if (datosNuevos.isTodoElDia()) {
            actual.setHoraInicio(null);
            actual.setHoraFin(null);
        } else {
            if (datosNuevos.getHoraInicio() == null || datosNuevos.getHoraFin() == null) {
                throw new RuntimeException("Si no es todo el día, las horas son obligatorias");
            }
            if (datosNuevos.getHoraInicio().isAfter(datosNuevos.getHoraFin())) {
                throw new RuntimeException("La hora inicio no puede ser mayor a la fin");
            }
            actual.setHoraInicio(datosNuevos.getHoraInicio());
            actual.setHoraFin(datosNuevos.getHoraFin());
        }

        // 4. Actualizar Relaciones (Grupo y Servicio)
        if (datosNuevos.getGrupo() != null && datosNuevos.getGrupo().getId() != null) {
            Grupo g = grupoRepository.findById(datosNuevos.getGrupo().getId())
                    .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
            actual.setGrupo(g);
        } else {
            actual.setGrupo(null);
        }

        if (datosNuevos.getServicio() != null && datosNuevos.getServicio().getIdServicio() != null) {
            Servicio s = servicioRepository.findById(datosNuevos.getServicio().getIdServicio())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
            actual.setServicio(s);
        } else {
            actual.setServicio(null);
        }

        // 5. IMPORTANTE: Cancelar citas conflictivas tras la actualización
        cancelarCitasConflictivas(actual);

        return bloqueoRepository.save(actual);
    }

    @Override
    @Transactional
    public List<BloqueoHorario> crearBloqueoRango(BloqueoHorario base, LocalDate fechaFin) {
        if (fechaFin == null || !fechaFin.isAfter(base.getFecha())) {
            // Si no hay fecha fin o es anterior/igual, tratamos como un solo día
            return List.of(crearBloqueo(base));
        }

        java.util.List<BloqueoHorario> creados = new java.util.ArrayList<>();
        LocalDate actual = base.getFecha();

        while (!actual.isAfter(fechaFin)) {
            BloqueoHorario nuevo = new BloqueoHorario();
            nuevo.setFecha(actual);
            nuevo.setHoraInicio(base.getHoraInicio());
            nuevo.setHoraFin(base.getHoraFin());
            nuevo.setTodoElDia(base.isTodoElDia());
            nuevo.setMotivo(base.getMotivo());
            nuevo.setGrupo(base.getGrupo());
            nuevo.setServicio(base.getServicio());

            creados.add(crearBloqueo(nuevo));
            actual = actual.plusDays(1);
        }

        return creados;
    }
}
