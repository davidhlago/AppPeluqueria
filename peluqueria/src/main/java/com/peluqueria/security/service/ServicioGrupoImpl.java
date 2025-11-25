package com.peluqueria.security.service;

import com.peluqueria.entity.Grupo;
import com.peluqueria.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioGrupoImpl implements ServicioGrupo {

    @Autowired
    private GrupoRepository grupoRepository;

    @Override
    public Grupo guardarGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    @Override
    public List<Grupo> obtenerTodosLosGrupos() {
        return grupoRepository.findAll();
    }

    @Override
    public Grupo obtenerGrupoPorId(Long id) {
        return grupoRepository.findById(id).orElseThrow();
    }

    @Override
    public void eliminarGrupo(Long id) {
        grupoRepository.deleteById(id);
    }

    @Override
    public List<Grupo> buscarPorCurso(String texto) {
        return grupoRepository.buscarPorCurso(texto);
    }
}