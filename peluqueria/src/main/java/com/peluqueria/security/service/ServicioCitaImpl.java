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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        // 1. Validaciones básicas (IDs existen)
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

        // 2. Validar que la fecha coincida con el día del horario (Lunes, Martes...)
        String diaSemanaFecha = traducirDia(citaIncoming.getFecha().getDayOfWeek());
        if (!diaSemanaFecha.equalsIgnoreCase(horario.getDiasSemana())) {
            throw new HorarioException("La fecha no coincide con el día del horario (" + horario.getDiasSemana() + ")");
        }

        // 3. CALCULAR LOS BLOQUES QUE SE VAN A GASTAR
        Servicio servicio = horario.getServicio();
        LocalTime inicioSolicitado = citaIncoming.getHoraInicio();
        if(inicioSolicitado == null) throw new CitaException("Debe indicar hora de inicio");

        // Aquí multiplicamos los bloques del servicio por 15 minutos.
        // Ejemplo: Si el servicio son 4 bloques -> 4 * 15 = 60 minutos ocupados.
        int duracionMinutos = (servicio.getDuracionBloques() > 0) ? servicio.getDuracionBloques() * MINUTOS_POR_BLOQUE : MINUTOS_POR_BLOQUE;

        LocalTime finSolicitado = inicioSolicitado.plusMinutes(duracionMinutos);

        // 4. Validar que la cita entera cabe dentro del turno (ej. no termina a las 14:15 si cierran a las 14:00)
        if (inicioSolicitado.isBefore(horario.getHoraInicio()) || finSolicitado.isAfter(horario.getHoraFin())) {
            throw new HorarioException("La hora seleccionada está fuera del turno del horario.");
        }

        // 5. VALIDACIÓN DE CUPO (Aquí es donde se 'restan' los bloques)
        // Esta consulta cuenta cuántas personas tienen YA ocupado ESE rango de tiempo.
        long citasSimultaneas = citaRepository.countCitasConflictivas(horario, citaIncoming.getFecha(), inicioSolicitado, finSolicitado);

        // Si cupoMaximo es 2, y citasSimultaneas ya es 2, significa que no quedan bloques libres para ti.
        if (citasSimultaneas >= horario.getCupoMaximo()) {
            throw new CitaException("No hay hueco disponible. El cupo está completo para ese tramo horario.");
        }

        // 6. Si pasa la validación, GUARDAMOS.
        // Al guardar, estos bloques quedan automáticamente "restados" para la siguiente persona que pregunte.
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
        int duracionMinutos = (servicio.getDuracionBloques() > 0) ? servicio.getDuracionBloques() * MINUTOS_POR_BLOQUE : MINUTOS_POR_BLOQUE;

        List<LocalTime> huecosLibres = new ArrayList<>();
        LocalTime horaActual = horario.getHoraInicio();
        LocalTime horaFinTurno = horario.getHoraFin();

        // Recorremos el horario cada 15 minutos para ver dónde caben los bloques del servicio
        while (!horaActual.plusMinutes(duracionMinutos).isAfter(horaFinTurno)) {
            LocalTime finBloque = horaActual.plusMinutes(duracionMinutos);

            // Verificamos si los bloques necesarios están libres
            long conflictos = citaRepository.countCitasConflictivas(horario, fecha, horaActual, finBloque);

            if (conflictos < horario.getCupoMaximo()) {
                huecosLibres.add(horaActual);
            }

            horaActual = horaActual.plusMinutes(MINUTOS_POR_BLOQUE);
        }

        return huecosLibres;
    }

    // ... Resto de métodos (modificar, gestionarEstado, etc.) ...

    @Override
    public Cita modificarCita(Long id, Cita citaDetalles) {
        Cita cita = obtenerPorId(id);
        if (citaDetalles.getFecha() != null) cita.setFecha(citaDetalles.getFecha());
        return citaRepository.save(cita);
    }

    @Override
    public Cita gestionarEstadoCita(Long idCita, int opcion) {
        Cita cita = obtenerPorId(idCita);
        if(opcion == 0) cita.setEstado("CONFIRMADA");
        else if(opcion == 1) cita.setEstado("CANCELADA");
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