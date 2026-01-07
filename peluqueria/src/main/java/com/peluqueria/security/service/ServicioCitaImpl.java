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

    @Override
    public Cita crearCita(Cita citaIncoming) {
        // Validaciones iniciales
        if (citaIncoming.getHorarioSemanal() == null || citaIncoming.getHorarioSemanal().getIdHorarioSemana() == null) {
            throw new HorarioException("Debe especificar el ID del horario semanal.");
        }
        if (citaIncoming.getCliente() == null || citaIncoming.getCliente().getId() == null) {
            throw new CitaException("Debe especificar el ID del cliente.");
        }

        // Buscamos horario y cliente
        HorarioSemanal horario = horarioRepository.findById(citaIncoming.getHorarioSemanal().getIdHorarioSemana())
                .orElseThrow(() -> new HorarioException("No existe un horario con el ID proporcionado."));

        Cliente cliente = clienteRepository.findById(citaIncoming.getCliente().getId())
                .orElseThrow(() -> new CitaException("No existe un cliente con el ID proporcionado."));

        // Validar lógica de Horario
        Servicio servicio = horario.getServicio();
        if (servicio == null) {
            throw new HorarioException("Error interno: El horario no tiene servicio asociado.");
        }

        String diaSemanaFecha = traducirDia(citaIncoming.getFecha().getDayOfWeek());
        if (!diaSemanaFecha.equalsIgnoreCase(horario.getDiasSemana())) {
            throw new HorarioException("La fecha (" + diaSemanaFecha + ") no coincide con el día del horario (" + horario.getDiasSemana() + ").");
        }

        LocalTime horaInicio = horario.getHoraInicio();
        int minutos = (servicio.getDuracionBloques() > 0) ? servicio.getDuracionBloques() * 30 : 30;
        LocalTime horaFin = horaInicio.plusMinutes(minutos);

        // Validar lógica de Cita (Cupos)
        long citasSimultaneas = citaRepository.countCitasConflictivas(horario.getIdHorarioSemana(), citaIncoming.getFecha(), horaInicio, horaFin);
        if (citasSimultaneas >= horario.getCupoMaximo()) {
            throw new CitaException("El cupo está completo para este horario.");
        }

        // Guardar
        citaIncoming.setCliente(cliente);
        citaIncoming.setHoraInicio(horaInicio);
        citaIncoming.setHoraFin(horaFin);
        citaIncoming.setGrupo(horario.getGrupo());
        citaIncoming.setHorarioSemanal(horario);
        citaIncoming.setEstado("PENDIENTE");

        return citaRepository.save(citaIncoming);
    }

    @Override
    public Cita modificarCita(Long id, Cita citaDetalles) {
        Cita cita = obtenerPorId(id);
        if (citaDetalles.getFecha() != null) {
            cita.setFecha(citaDetalles.getFecha());
            // Nota: Aquí se podría añadir más lógica y lanzar HorarioException si la nueva fecha no cuadra
        }
        return citaRepository.save(cita);
    }

    @Override
    public Cita gestionarEstadoCita(Long idCita, int opcion) {
        Cita cita = obtenerPorId(idCita);

        if (opcion == 0) {
            cita.setEstado("CONFIRMADA");
        } else if (opcion == 1) {
            cita.setEstado("CANCELADA");
        } else {
            throw new CitaException("Opción no válida. Use 0 para Confirmar o 1 para Cancelar.");
        }

        return citaRepository.save(cita);
    }

    @Override
    public void cancelarCita(Long id) {
        gestionarEstadoCita(id, 1);
    }

    @Override
    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new CitaException("Cita no encontrada con ID: " + id));
    }

    @Override
    public List<Cita> obtenerTodas() { return citaRepository.findAll(); }
    @Override
    public List<Cita> obtenerPorCliente(Long id) { return citaRepository.findByCliente_Id(id); }
    @Override
    public List<Cita> obtenerPorGrupo(Long id) { return citaRepository.findByGrupo_Id(id); }
    @Override
    public List<Cita> obtenerPorAlumno(Long id) { return new ArrayList<>(); }

    @Override
    public List<LocalTime> obtenerHuecosDisponibles(LocalDate f, Long s, Long g) {
        // Implementación pendiente o vacía según tu lógica
        return new ArrayList<>();
    }

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