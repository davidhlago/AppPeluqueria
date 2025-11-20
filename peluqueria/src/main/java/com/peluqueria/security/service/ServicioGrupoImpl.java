package com.peluqueria.security.service;

import com.peluqueria.entity.Grupo;
import com.peluqueria.repository.GrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioGrupoImpl {

    private final GrupoRepository grupoRepository;

    public ServicioGrupoImpl(GrupoRepository grupoRepository) {
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

    public Grupo actualizarGrupo(Long id, Grupo detalles) {
        Grupo grupo = obtenerGrupoPorId(id);
        grupo.setCurso(detalles.getCurso());
        grupo.setTurno(detalles.getTurno());
        return grupoRepository.save(grupo);
    }

    public void eliminarGrupo(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        grupoRepository.deleteById(id);
    }
}
