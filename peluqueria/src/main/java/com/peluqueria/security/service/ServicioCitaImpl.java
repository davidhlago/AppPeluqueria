package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.entity.Servicio;
import com.peluqueria.exception.CitaException;
import com.peluqueria.exception.HorarioException;
import com.peluqueria.repository.CitaRepository;
import com.peluqueria.repository.ClienteRepository;
import com.peluqueria.repository.HorarioSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServicioCitaImpl implements ServicioCita {

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private HorarioSemanalRepository horarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    // 1 bloque = 15 minutos
    private static final int MINUTOS_POR_BLOQUE = 15;

    @Override
    public Cita crearCita(Cita citaIncoming) {
        if (citaIncoming.getHorarioSemanal() == null || citaIncoming.getHorarioSemanal().getIdHorarioSemana() == null) {
            throw new HorarioException("Debe especificar el ID del horario semanal");
        }
        HorarioSemanal horario = horarioRepository.findById(citaIncoming.getHorarioSemanal().getIdHorarioSemana())
                .orElseThrow(() -> new HorarioException("Horario no encontrado"));

        if (citaIncoming.getCliente() == null || citaIncoming.getCliente().getId() == null) {
            throw new CitaException("Debe especificar el ID del cliente");
        }
        Cliente cliente = clienteRepository.findById(citaIncoming.getCliente().getId())
                .orElseThrow(() -> new CitaException("Cliente no encontrado"));

        String diaSemanaFecha = traducirDia(citaIncoming.getFecha().getDayOfWeek());
        if (!diaSemanaFecha.equalsIgnoreCase(horario.getDiasSemana())) {
            throw new HorarioException("La fecha no coincide con el día del horario (" + horario.getDiasSemana() + ")");
        }

        Servicio servicio = horario.getServicio();
        LocalTime inicioSolicitado = citaIncoming.getHoraInicio();
        if (inicioSolicitado == null) throw new CitaException("Debe indicar hora de inicio");

        int duracionMinutos = (servicio.getDuracionBloques() > 0)
                ? servicio.getDuracionBloques() * MINUTOS_POR_BLOQUE
                : MINUTOS_POR_BLOQUE;

        LocalTime finSolicitado = inicioSolicitado.plusMinutes(duracionMinutos);

        if (inicioSolicitado.isBefore(horario.getHoraInicio()) || finSolicitado.isAfter(horario.getHoraFin())) {
            throw new HorarioException("La hora seleccionada está fuera del turno del horario.");
        }

        long citasSimultaneas = citaRepository.countCitasConflictivas(horario, citaIncoming.getFecha(), inicioSolicitado, finSolicitado);

        if (citasSimultaneas >= horario.getCupoMaximo()) {
            throw new CitaException("No hay hueco disponible. El cupo está completo para ese tramo horario.");
        }

        citaIncoming.setCliente(cliente);
        citaIncoming.setHoraFin(finSolicitado);
        citaIncoming.setGrupo(horario.getGrupo());
        citaIncoming.setHorarioSemanal(horario);
        citaIncoming.setEstado("CONFIRMADA");

        return citaRepository.save(citaIncoming);
    }

    @Override
    public List<LocalTime> obtenerHuecosDisponibles(LocalDate fecha, Long idHorarioSemana, Long idGrupoIgnorado) {
        HorarioSemanal horario = horarioRepository.findById(idHorarioSemana)
                .orElseThrow(() -> new HorarioException("Horario no encontrado"));

        String diaSemanaFecha = traducirDia(fecha.getDayOfWeek());
        if (!diaSemanaFecha.equalsIgnoreCase(horario.getDiasSemana())) {
            throw new HorarioException("La fecha no coincide con el día del horario");
        }

        Servicio servicio = horario.getServicio();
        int duracionMinutos = (servicio.getDuracionBloques() > 0)
                ? servicio.getDuracionBloques() * MINUTOS_POR_BLOQUE
                : MINUTOS_POR_BLOQUE;

        List<LocalTime> huecosLibres = new ArrayList<>();
        LocalTime horaActual = horario.getHoraInicio();
        LocalTime horaFinTurno = horario.getHoraFin();

        while (!horaActual.plusMinutes(duracionMinutos).isAfter(horaFinTurno)) {
            LocalTime finBloque = horaActual.plusMinutes(duracionMinutos);

            long conflictos = citaRepository.countCitasConflictivas(horario, fecha, horaActual, finBloque);

            if (conflictos < horario.getCupoMaximo()) {
                huecosLibres.add(horaActual);
            }

            horaActual = horaActual.plusMinutes(MINUTOS_POR_BLOQUE);
        }

        return huecosLibres;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerHuecosPorServicioYFecha(Long idServicio, LocalDate fecha) {
        String diaEspanol = traducirDia(fecha.getDayOfWeek());

        var horarios = horarioRepository.buscarPorServicioYDia(idServicio, diaEspanol);
        List<Map<String, Object>> huecosDisponibles = new ArrayList<>();

        for (HorarioSemanal horario : horarios) {
            Servicio servicio = horario.getServicio();
            if (servicio == null) continue;

            int duracionMinutos = (servicio.getDuracionBloques() > 0)
                    ? servicio.getDuracionBloques() * MINUTOS_POR_BLOQUE
                    : MINUTOS_POR_BLOQUE;

            LocalTime horaActual = horario.getHoraInicio();
            LocalTime horaFinTurno = horario.getHoraFin();

            while (!horaActual.plusMinutes(duracionMinutos).isAfter(horaFinTurno)) {
                LocalTime finBloque = horaActual.plusMinutes(duracionMinutos);

                long conflictos = citaRepository.countCitasConflictivas(horario, fecha, horaActual, finBloque);

                if (conflictos < horario.getCupoMaximo()) {
                    int plazasRestantes = (int) (horario.getCupoMaximo() - conflictos);

                    huecosDisponibles.add(Map.of(
                            "hora", horaActual.toString(),
                            "idHorario", horario.getIdHorarioSemana(),
                            "plazasRestantes", plazasRestantes
                    ));
                }

                horaActual = horaActual.plusMinutes(MINUTOS_POR_BLOQUE);
            }
        }
        return huecosDisponibles;
    }

    @Override
    public Cita modificarCita(Long id, Cita citaDetalles) {
        Cita cita = obtenerPorId(id);
        if (citaDetalles.getFecha() != null) cita.setFecha(citaDetalles.getFecha());
        return citaRepository.save(cita);
    }

    @Override
    public Cita gestionarEstadoCita(Long idCita, int opcion) {
        Cita cita = obtenerPorId(idCita);
        if (opcion == 0) cita.setEstado("CONFIRMADA");
        else if (opcion == 1) cita.setEstado("CANCELADA");
        else throw new CitaException("Estado no válido");
        return citaRepository.save(cita);
    }

    @Override
    public void cancelarCita(Long id) { gestionarEstadoCita(id, 1); }

    @Override
    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id).orElseThrow(() -> new CitaException("Cita no encontrada"));
    }

    @Override
    public List<Cita> obtenerTodas() { return citaRepository.findAll(); }

    @Override
    public List<Cita> obtenerPorCliente(Long id) { return citaRepository.findByCliente_Id(id); }

    @Override
    public List<Cita> obtenerPorGrupo(Long id) { return citaRepository.findByGrupo_Id(id); }

    @Override
    public List<Cita> obtenerPorAlumno(Long id) { return new ArrayList<>(); }

    private String traducirDia(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "";
        }
    }
}
