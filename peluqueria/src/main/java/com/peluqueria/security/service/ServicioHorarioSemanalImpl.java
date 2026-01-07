package com.peluqueria.security.service;

import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.repository.HorarioSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioHorarioSemanalImpl implements ServicioHorarioSemanal {

    @Autowired
    private HorarioSemanalRepository horarioRepository;

    @Override
    public HorarioSemanal guardarHorario(HorarioSemanal horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public List<HorarioSemanal> obtenerTodos() {
        return horarioRepository.findAll();
    }

    @Override
    public List<HorarioSemanal> obtenerPorGrupo(Long idGrupo) {
        // Asegúrate de que en tu repositorio el método se llame findByGrupo_Id
        return horarioRepository.findByGrupo_Id(idGrupo);
    }

    @Override
    public List<HorarioSemanal> obtenerPorServicio(Long idServicio) {
        // Asegúrate de que en tu repositorio el método se llame findByServicio_IdServicio
        return horarioRepository.findByServicio_IdServicio(idServicio);
    }

    @Override
    public HorarioSemanal obtenerPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Horario no encontrado"));
    }

    @Override
    public void eliminarHorario(Long id) {
        horarioRepository.deleteById(id);
    }
}