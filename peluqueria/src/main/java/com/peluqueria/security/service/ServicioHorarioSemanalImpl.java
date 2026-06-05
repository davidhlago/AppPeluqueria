package com.peluqueria.security.service;

import com.peluqueria.entity.Grupo;
import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.entity.Servicio;
import com.peluqueria.exception.HorarioException;
import com.peluqueria.repository.GrupoRepository;
import com.peluqueria.repository.HorarioSemanalRepository;
import com.peluqueria.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioHorarioSemanalImpl implements ServicioHorarioSemanal {

    @Autowired
    private HorarioSemanalRepository horarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Override
    @Transactional
    public HorarioSemanal guardarHorario(HorarioSemanal horario) {
        // 1. Validaciones básicas
        if (horario == null) throw new HorarioException("El horario no puede ser nulo");
        if (horario.getCupoMaximo() < 0) throw new HorarioException("El cupo no puede ser negativo");

        // 2. ASIGNAR ENTIDADES REALES (Evita TransientObjectException)
        // Buscamos el Servicio real
        if (horario.getServicio() != null && horario.getServicio().getIdServicio() != null) {
            Servicio s = servicioRepository.findById(horario.getServicio().getIdServicio())
                    .orElseThrow(() -> new HorarioException("Servicio no encontrado ID: " + horario.getServicio().getIdServicio()));
            horario.setServicio(s);
        } else {
            throw new HorarioException("Debes indicar un Servicio válido");
        }

        // Buscamos el Grupo real
        if (horario.getGrupo() != null && horario.getGrupo().getId() != null) {
            Grupo g = grupoRepository.findById(horario.getGrupo().getId())
                    .orElseThrow(() -> new HorarioException("Grupo no encontrado ID: " + horario.getGrupo().getId()));
            horario.setGrupo(g);
        } else {
            throw new HorarioException("Debes indicar un Grupo válido");
        }

        // 3. VALIDACIÓN DE SOLAPAMIENTO DE HORAS
        validarSolapamiento(horario);

        return horarioRepository.save(horario);
    }

    @Override
    @Transactional
    public HorarioSemanal actualizarHorario(Long id, HorarioSemanal datosNuevos) {
        HorarioSemanal existente = horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioException("Horario no encontrado con ID: " + id));

        // Actualizamos campos simples
        existente.setCupoMaximo(datosNuevos.getCupoMaximo());
        existente.setDiasSemana(datosNuevos.getDiasSemana());
        existente.setHoraInicio(datosNuevos.getHoraInicio());
        existente.setHoraFin(datosNuevos.getHoraFin());

        // Si intentan cambiar el servicio, buscamos el nuevo
        if (datosNuevos.getServicio() != null && datosNuevos.getServicio().getIdServicio() != null) {
            Servicio s = servicioRepository.findById(datosNuevos.getServicio().getIdServicio())
                    .orElseThrow(() -> new HorarioException("Servicio no encontrado"));
            existente.setServicio(s);
        }

        // Si intentan cambiar el grupo, buscamos el nuevo
        if (datosNuevos.getGrupo() != null && datosNuevos.getGrupo().getId() != null) {
            Grupo g = grupoRepository.findById(datosNuevos.getGrupo().getId())
                    .orElseThrow(() -> new HorarioException("Grupo no encontrado"));
            existente.setGrupo(g);
        }

        // 4. VALIDAMOS SOLAPAMIENTO TAMBIÉN AL ACTUALIZAR
        validarSolapamiento(existente);

        return horarioRepository.save(existente);
    }

    // Método privado para reutilizar la lógica de solapamiento
    private void validarSolapamiento(HorarioSemanal horario) {
        List<HorarioSemanal> horariosExistentes = horarioRepository.findByGrupoAndDia(
                horario.getGrupo().getId(),
                horario.getDiasSemana()
        );

        for (HorarioSemanal existente : horariosExistentes) {
            // Ignoramos si es el mismo registro que estamos editando
            if (horario.getIdHorarioSemana() != null &&
                    horario.getIdHorarioSemana().equals(existente.getIdHorarioSemana())) {
                continue;
            }

            // Lógica: (NuevoInicio < ViejoFin) Y (NuevoFin > ViejoInicio)
            boolean solapa = horario.getHoraInicio().isBefore(existente.getHoraFin()) &&
                    horario.getHoraFin().isAfter(existente.getHoraInicio());

            if (solapa) {
                throw new HorarioException("CONFLICTO: El grupo ya tiene clase de "
                        + existente.getHoraInicio() + " a " + existente.getHoraFin());
            }
        }
    }

    @Override
    public List<HorarioSemanal> obtenerTodos() {
        return horarioRepository.findAllOptimized();
    }

    @Override
    public List<HorarioSemanal> obtenerPorGrupo(Long idGrupo) {
        return horarioRepository.findByGrupo_IdOptimized(idGrupo);
    }

    @Override
    public List<HorarioSemanal> obtenerPorServicio(Long idServicio) {
        return horarioRepository.findByServicio_IdServicioOptimized(idServicio);
    }

    @Override
    public HorarioSemanal obtenerPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new HorarioException("Horario no encontrado con ID: " + id));
    }

    @Override
    public void eliminarHorario(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new HorarioException("No existe el horario con ID: " + id);
        }
        horarioRepository.deleteById(id);
    }
}