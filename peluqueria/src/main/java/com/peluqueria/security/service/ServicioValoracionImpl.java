package com.peluqueria.security.service;

import com.peluqueria.entity.Cita;
import com.peluqueria.entity.Valoracion;
import com.peluqueria.exception.CitaException;
import com.peluqueria.exception.ValoracionException;
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
    public Valoracion crearValoracion(Valoracion valoracion, Long idCita, Long idCliente) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new CitaException("Cita no encontrada"));

        // VALIDACIÓN 1: ¿La cita pertenece al cliente logueado? (Seguridad)
        if (!cita.getCliente().getId().equals(idCliente)) {
            throw new ValoracionException("No tienes permiso para valorar una cita que no te pertenece.");
        }

        // VALIDACIÓN 2: ¿La cita está cancelada? (Lógica de negocio)
        if ("CANCELADA".equalsIgnoreCase(cita.getEstado())) {
            throw new ValoracionException("No se puede valorar una cita que ha sido cancelada.");
        }

        // VALIDACIÓN 3: ¿Ya existe una valoración para esta cita? (Integridad)
        List<Valoracion> existentes = valoracionRepository.findByCita(cita);
        if (!existentes.isEmpty()) {
            throw new ValoracionException("Esta cita ya ha sido valorada.");
        }

        // Validación de rango de puntuación
        validarPuntuacion(valoracion.getTratoPersonal(), "Trato Personal");
        validarPuntuacion(valoracion.getDesarrolloServicio(), "Desarrollo del Servicio");
        validarPuntuacion(valoracion.getClaridadComunicacion(), "Claridad en la Comunicación");
        validarPuntuacion(valoracion.getLimpiezaOrganizacion(), "Limpieza y Organización");
        validarPuntuacion(valoracion.getGeneral(), "General");

        // Seteamos datos automáticos
        valoracion.setCita(cita);
        if (valoracion.getFechaValoracion() == null) {
            valoracion.setFechaValoracion(LocalDateTime.now());
        }

        // Al valorar, marcamos la cita como COMPLETADA automáticamente
        cita.setEstado("COMPLETADA");
        citaRepository.save(cita);

        return valoracionRepository.save(valoracion);
    }

    @Override
    public Valoracion crearValoracion(Valoracion valoracion, Long idCita) {
        return null;
    }

    @Override
    public Valoracion actualizarValoracion(Long idValoracion, Valoracion datos) {
        Valoracion existente = valoracionRepository.findById(idValoracion)
                .orElseThrow(() -> new RuntimeException("Valoración no encontrada"));

        if (datos.getTratoPersonal() >= 1.0 && datos.getTratoPersonal() <= 5.0) {
            existente.setTratoPersonal(datos.getTratoPersonal());
        }
        if (datos.getDesarrolloServicio() >= 1.0 && datos.getDesarrolloServicio() <= 5.0) {
            existente.setDesarrolloServicio(datos.getDesarrolloServicio());
        }
        if (datos.getClaridadComunicacion() >= 1.0 && datos.getClaridadComunicacion() <= 5.0) {
            existente.setClaridadComunicacion(datos.getClaridadComunicacion());
        }
        if (datos.getLimpiezaOrganizacion() >= 1.0 && datos.getLimpiezaOrganizacion() <= 5.0) {
            existente.setLimpiezaOrganizacion(datos.getLimpiezaOrganizacion());
        }
        if (datos.getGeneral() >= 1.0 && datos.getGeneral() <= 5.0) {
            existente.setGeneral(datos.getGeneral());
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

    private void validarPuntuacion(double puntuacion, String campo) {
        if (puntuacion < 1.0 || puntuacion > 5.0) {
            throw new IllegalArgumentException("La puntuación de " + campo + " debe estar entre 1.0 y 5.0");
        }
    }
}