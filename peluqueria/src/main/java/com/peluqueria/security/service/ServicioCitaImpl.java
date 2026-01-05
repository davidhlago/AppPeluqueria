package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.repository.CitaRepository;
import com.peluqueria.repository.HorarioSemanalRepository;
import com.peluqueria.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
public class ServicioCitaImpl implements ServicioCita {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HorarioSemanalRepository horarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public Cita crearCita(Cita cita) {
        validarHorario(cita, null);
        cita.setEstado("PENDIENTE");
        return citaRepository.save(cita);
    }

    @Override
    public Cita modificarCita(Long id, Cita citaDetalles) {
        Cita citaExistente = obtenerPorId(id);

        citaExistente.setFecha(citaDetalles.getFecha());
        citaExistente.setHoraInicio(citaDetalles.getHoraInicio());
        citaExistente.setHoraFin(citaDetalles.getHoraFin());
        citaExistente.setServicio(citaDetalles.getServicio());

        if (citaDetalles.getGrupo() != null) citaExistente.setGrupo(citaDetalles.getGrupo());
        if (citaDetalles.getAlumno() != null) citaExistente.setAlumno(citaDetalles.getAlumno());

        validarHorario(citaExistente, id);

        return citaRepository.save(citaExistente);
    }

    private void validarHorario(Cita cita, Long citaIdExcluir) {
        String diaSemanaCalculado = cita.getFecha().getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        final String diaSemana = diaSemanaCalculado.substring(0, 1).toUpperCase()
                + diaSemanaCalculado.substring(1).toLowerCase();

        HorarioSemanal horario = horarioRepository.buscarDisponibilidad(
                cita.getGrupo().getId(),
                cita.getServicio().getIdServicio(),
                diaSemana
        ).orElseThrow(() -> new RuntimeException("El grupo no ofrece el servicio con ID "
                + cita.getServicio().getIdServicio() + " los " + diaSemana));

        if (cita.getHoraInicio().isBefore(horario.getHoraInicio()) ||
                cita.getHoraFin().isAfter(horario.getHoraFin())) {
            throw new RuntimeException("Horario fuera de turno. Hoy (" + diaSemana + ") es de "
                    + horario.getHoraInicio() + " a " + horario.getHoraFin());
        }

        long citasActuales = citaRepository.contarCitasPorDia(
                cita.getGrupo().getId(),
                cita.getServicio().getIdServicio(),
                cita.getFecha()
        );

        if (citaIdExcluir != null) {
            Cita citaVieja = obtenerPorId(citaIdExcluir);
            if (citaVieja.getFecha().equals(cita.getFecha()) &&
                    citaVieja.getServicio().getIdServicio().equals(cita.getServicio().getIdServicio()) &&
                    citaVieja.getGrupo().getId().equals(cita.getGrupo().getId())) {
                citasActuales--;
            }
        }

        if (horario.getCupoMaximo() != null && citasActuales >= horario.getCupoMaximo()) {
            throw new RuntimeException("Cupo completo. Solo se permiten " + horario.getCupoMaximo()
                    + " citas de este servicio los " + diaSemana);
        }

        List<Cita> conflictos;
        if (citaIdExcluir == null) {
            conflictos = citaRepository.encontrarCitasSolapadas(
                    cita.getGrupo().getId(), cita.getFecha(), cita.getHoraInicio(), cita.getHoraFin());
        } else {
            conflictos = citaRepository.encontrarCitasSolapadasEditar(
                    cita.getGrupo().getId(), cita.getFecha(), cita.getHoraInicio(), cita.getHoraFin(), citaIdExcluir);
        }

        if (!conflictos.isEmpty()) {
            throw new RuntimeException("Ya existe otra cita reservada en ese horario.");
        }
    }

    @Override
    public List<LocalTime> obtenerHuecosDisponibles(LocalDate fecha, Long servicioId, Long grupoId) {
        String diaSemanaCalculado = fecha.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String diaSemana = diaSemanaCalculado.substring(0, 1).toUpperCase()
                + diaSemanaCalculado.substring(1).toLowerCase();

        HorarioSemanal horario = horarioRepository.buscarDisponibilidad(grupoId, servicioId, diaSemana)
                .orElseThrow(() -> new RuntimeException("No hay horario disponible para este día."));

        long citasActuales = citaRepository.contarCitasPorDia(grupoId, servicioId, fecha);
        if (horario.getCupoMaximo() != null && citasActuales >= horario.getCupoMaximo()) {
            return new ArrayList<>();
        }

        long duracionMinutos = 60;

        List<Cita> citasOcupadas = citaRepository.encontrarCitasSolapadas(
                grupoId, fecha, LocalTime.MIN, LocalTime.MAX);

        List<LocalTime> huecosLibres = new ArrayList<>();
        LocalTime horaActual = horario.getHoraInicio();

        while (!horaActual.plusMinutes(duracionMinutos).isAfter(horario.getHoraFin())) {
            LocalTime finTurno = horaActual.plusMinutes(duracionMinutos);
            boolean estaOcupado = false;

            for (Cita cita : citasOcupadas) {
                if (horaActual.isBefore(cita.getHoraFin()) && finTurno.isAfter(cita.getHoraInicio())) {
                    estaOcupado = true;
                    break;
                }
            }
            if (!estaOcupado) {
                huecosLibres.add(horaActual);
            }
            horaActual = horaActual.plusMinutes(duracionMinutos);
        }
        return huecosLibres;
    }

    @Override
    public List<Cita> obtenerTodas() { return citaRepository.findAll(); }

    @Override
    public List<Cita> obtenerPorCliente(Long clienteId) { return citaRepository.findByClienteId(clienteId); }

    @Override
    public List<Cita> obtenerPorGrupo(Long grupoId) { return citaRepository.findByGrupoId(grupoId); }

    @Override
    public List<Cita> obtenerPorAlumno(Long alumnoId) { return citaRepository.findByAlumnoId(alumnoId); }

    @Override
    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));
    }

    @Override
    public void cancelarCita(Long id) {
        Cita cita = obtenerPorId(id);
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
    }
}