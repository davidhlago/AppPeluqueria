package com.peluqueria.service;

import com.peluqueria.project.modelo.Grupo;
import java.util.List;

public interface ServicioGrupo {
    Grupo guardarGrupo(Grupo grupo);
    void eliminarGrupo(Long id);
    Grupo obtenerGrupoPorId(Long id);
    List<Grupo> obtenerTodosLosGrupos();
}