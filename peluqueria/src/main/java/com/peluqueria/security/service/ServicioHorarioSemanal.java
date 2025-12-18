package com.peluqueria.security.service;

import com.peluqueria.entity.HorarioSemanal;
import java.util.List;

public interface ServicioHorarioSemanal {
    HorarioSemanal guardarHorario(HorarioSemanal horario);
    List<HorarioSemanal> obtenerTodos();
    List<HorarioSemanal> obtenerPorGrupo(Long idGrupo);
    List<HorarioSemanal> obtenerPorServicio(Long idServicio);
    void eliminarHorario(Long id);
    HorarioSemanal obtenerPorId(Long id);
}