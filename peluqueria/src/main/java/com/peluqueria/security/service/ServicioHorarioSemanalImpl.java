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
        // Cambiamos findAll() por nuestro nuevo método optimizado
        return horarioRepository.findAllOptimized();
    }

    @Override
    public List<HorarioSemanal> obtenerPorGrupo(Long idGrupo) {
        return horarioRepository.findByGrupo_IdOptimized(idGrupo);
    }
    @Override
    public List<HorarioSemanal> obtenerPorServicio(Long idServicio) {
        // Cambia el nombre al que acabamos de crear en el repository
        return horarioRepository.findByServicio_IdServicioOptimized(idServicio);
    }

    // En ServicioHorarioSemanalImpl.java
    @Override
    public HorarioSemanal actualizarHorario(Long id, HorarioSemanal datosNuevos) {
        HorarioSemanal existente = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        existente.setCupoMaximo(datosNuevos.getCupoMaximo());
        existente.setDiasSemana(datosNuevos.getDiasSemana());
        existente.setHoraInicio(datosNuevos.getHoraInicio());
        existente.setHoraFin(datosNuevos.getHoraFin());
        // Mantenemos el servicio y grupo originales o los actualizamos si vienen en el body
        if(datosNuevos.getServicio() != null) existente.setServicio(datosNuevos.getServicio());
        if(datosNuevos.getGrupo() != null) existente.setGrupo(datosNuevos.getGrupo());

        return horarioRepository.save(existente);
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