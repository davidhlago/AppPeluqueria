package com.peluqueria.service;

import com.peluqueria.entity.Grupo;
import com.peluqueria.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioGrupo {

    private final GrupoRepository grupoRepository;

    public ServicioGrupo(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public Grupo guardarGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public List<Grupo> obtenerTodosLosGrupos() {
        return grupoRepository.findAll();
    }

    public Grupo obtenerGrupoPorId(Long id) {
        return grupoRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public void eliminarGrupo(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        grupoRepository.deleteById(id);
    }
}
