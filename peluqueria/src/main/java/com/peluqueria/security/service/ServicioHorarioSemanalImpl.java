package com.peluqueria.security.service;

import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.exception.HorarioException;
import com.peluqueria.repository.HorarioSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioHorarioSemanalImpl implements ServicioHorarioSemanal {

    @Autowired
    private HorarioSemanalRepository horarioRepository;

    @Override
    public HorarioSemanal guardarHorario(HorarioSemanal horario) {
        if (horario == null) {
            throw new HorarioException("Los datos del horario no pueden ser nulos.");
        }
        if (horario.getCupoMaximo() < 0) {
            throw new HorarioException("El cupo máximo no puede ser negativo.");
        }
        return horarioRepository.save(horario);
    }

    @Override
    public List<HorarioSemanal> obtenerTodos() {
        return horarioRepository.findAll();
    }

    @Override
    public List<HorarioSemanal> obtenerPorGrupo(Long idGrupo) {
        return horarioRepository.findByGrupo_Id(idGrupo);
    }

    @Override
    public List<HorarioSemanal> obtenerPorServicio(Long idServicio) {
        return horarioRepository.findByServicio_IdServicio(idServicio);
    }

    @Override
    public HorarioSemanal obtenerPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioException("Horario no encontrado con ID: " + id));
    }

    @Override
    public void eliminarHorario(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new HorarioException("No se puede eliminar. No existe un horario con ID: " + id);
        }
        horarioRepository.deleteById(id);
    }
}