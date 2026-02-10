package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import com.peluqueria.entity.Valoracion;
import com.peluqueria.exception.CitaException;
import com.peluqueria.repository.CitaRepository;
import com.peluqueria.repository.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicioValoracionImpl implements ServicioValoracion {

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Override
    public Valoracion crearValoracion(Valoracion valoracion, Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new CitaException("Cita no encontrada"));

        valoracion.setCita(cita);
        if (valoracion.getFechaValoracion() == null) {
            valoracion.setFechaValoracion(LocalDateTime.now());
        }

        if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }

        // Actualizar estado de la cita a COMPLETADA
        cita.setEstado("COMPLETADA");
        citaRepository.save(cita);

        return valoracionRepository.save(valoracion);
    }

    @Override
    public Valoracion actualizarValoracion(Long idValoracion, Valoracion datos) {
        Valoracion existente = valoracionRepository.findById(idValoracion)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));

        if (datos.getPuntuacion() >= 1 && datos.getPuntuacion() <= 5) {
            existente.setPuntuacion(datos.getPuntuacion());
        }
        if (datos.getComentario() != null) {
            existente.setComentario(datos.getComentario());
        }
        if (datos.getImagenBase64() != null) {
            existente.setImagenBase64(datos.getImagenBase64());
        }
        if (datos.getFechaValoracion() != null) {
            existente.setFechaValoracion(datos.getFechaValoracion());
        }

        return valoracionRepository.save(existente);
    }

    @Override
    public void eliminarValoracion(Long idValoracion) {
        if (!valoracionRepository.existsById(idValoracion)) {
            throw new RuntimeException("Valoración no encontrada");
        }
        valoracionRepository.deleteById(idValoracion);
    }

    @Override
    public List<Valoracion> obtenerPorCita(Long idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new CitaException("Cita no encontrada"));
        return valoracionRepository.findByCita(cita);
    }

    @Override
    public List<Valoracion> obtenerTodas() {
        return valoracionRepository.findAll();
    }
}
