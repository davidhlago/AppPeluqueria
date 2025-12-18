package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import java.util.List;

public interface ServicioCita {
    Cita crearCita(Cita cita);
    List<Cita> obtenerTodas();
    List<Cita> obtenerPorCliente(Long clienteId);
    List<Cita> obtenerPorGrupo(Long grupoId);
    List<Cita> obtenerPorAlumno(Long alumnoId);
    Cita obtenerPorId(Long id);
    void cancelarCita(Long id);
}