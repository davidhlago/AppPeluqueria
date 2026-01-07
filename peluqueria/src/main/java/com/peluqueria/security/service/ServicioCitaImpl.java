package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.entity.Servicio;
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
import java.util.NoSuchElementException;

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
        if (citaIncoming.getHorarioSemanal() == null || citaIncoming.getHorarioSemanal().getIdHorarioSemana() == null) {
            throw new RuntimeException("Debe especificar el id del horario semanal");
        }
        if (citaIncoming.getCliente() == null || citaIncoming.getCliente().getId() == null) {
            throw new RuntimeException("Debe especificar el id del cliente");
        }

        HorarioSemanal horario = horarioRepository.findById(citaIncoming.getHorarioSemanal().getIdHorarioSemana())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        Cliente cliente = clienteRepository.findById(citaIncoming.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Servicio servicio = horario.getServicio();
        LocalTime horaInicio = horario.getHoraInicio();

        String diaSemanaFecha = traducirDia(citaIncoming.getFecha().getDayOfWeek());
        if (!diaSemanaFecha.equalsIgnoreCase(horario.getDiasSemana())) {
            throw new RuntimeException("La fecha no coincide con el día del horario");
        }

        int minutos = (servicio.getDuracionBloques() > 0) ? servicio.getDuracionBloques() * 30 : 30;
        LocalTime horaFin = horaInicio.plusMinutes(minutos);

        long citasSimultaneas = citaRepository.countCitasConflictivas(horario.getIdHorarioSemana(), citaIncoming.getFecha(), horaInicio, horaFin);
        if (citasSimultaneas >= horario.getCupoMaximo()) {
            throw new RuntimeException("Cupo completo");
        }

        citaIncoming.setCliente(cliente);
        citaIncoming.setHoraInicio(horaInicio);
        citaIncoming.setHoraFin(horaFin);
        citaIncoming.setGrupo(horario.getGrupo());
        citaIncoming.setHorarioSemanal(horario);
        citaIncoming.setEstado("CONFIRMADA");

        return citaRepository.save(citaIncoming);
    }

    @Override
    public Cita modificarCita(Long id, Cita citaDetalles) {
        Cita cita = obtenerPorId(id);
        cita.setFecha(citaDetalles.getFecha());
        return citaRepository.save(cita);
    }

    @Override
    public Cita gestionarEstadoCita(Long idCita, int opcion) {
        Cita cita = obtenerPorId(idCita);
        if(opcion == 0) cita.setEstado("CONFIRMADA");
        else if(opcion == 1) cita.setEstado("CANCELADA");
        return citaRepository.save(cita);
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

    @Override
    public void cancelarCita(Long id) { gestionarEstadoCita(id, 1); }
    @Override
    public List<Cita> obtenerTodas() { return citaRepository.findAll(); }
    @Override
    public List<Cita> obtenerPorCliente(Long id) { return citaRepository.findByCliente_Id(id); }
    @Override
    public List<Cita> obtenerPorGrupo(Long id) { return citaRepository.findByGrupo_Id(id); }
    @Override
    public Cita obtenerPorId(Long id) { return citaRepository.findById(id).orElseThrow(); }
    @Override
    public List<Cita> obtenerPorAlumno(Long id) { return new ArrayList<>(); }
    @Override
    public List<LocalTime> obtenerHuecosDisponibles(LocalDate f, Long s, Long g) { return new ArrayList<>(); }
}