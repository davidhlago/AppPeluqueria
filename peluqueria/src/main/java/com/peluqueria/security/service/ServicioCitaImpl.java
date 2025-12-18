package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import com.peluqueria.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioCitaImpl implements ServicioCita {

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public Cita crearCita(Cita cita) {
        cita.setEstado("PENDIENTE");
        return citaRepository.save(cita);
    }

    @Override
    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    @Override
    public List<Cita> obtenerPorCliente(Long clienteId) {
        return citaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Cita> obtenerPorGrupo(Long grupoId) {
        return citaRepository.findByGrupoId(grupoId);
    }

    @Override
    public List<Cita> obtenerPorAlumno(Long alumnoId) {
        return citaRepository.findByAlumnoId(alumnoId);
    }

    @Override
    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cita no encontrada"));
    }

    @Override
    public void cancelarCita(Long id) {
        Cita cita = obtenerPorId(id);
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
    }
}