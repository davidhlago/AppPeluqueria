package com.peluqueria.security.service;

import com.peluqueria.entity.Grupo;
import java.util.List;

public interface ServicioGrupo {
    Grupo guardarGrupo(Grupo grupo);
    List<Grupo> obtenerTodosLosGrupos();
    Grupo obtenerGrupoPorId(Long id);
    void eliminarGrupo(Long id);
    List<Grupo> buscarPorCurso(String texto);
}