package com.peluqueria.controllers;

import com.peluqueria.entity.Valoracion;
import com.peluqueria.repository.ValoracionRepository;
import com.peluqueria.security.service.ServicioValoracion;
import com.peluqueria.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/valoraciones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ValoracionController {

    @Autowired
    private ServicioValoracion servicioValoracion;

    @Autowired
    private ValoracionRepository valoracionRepository;

    // Crear valoración para una cita concreta
    @PostMapping("/cita/{idCita}")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity<Valoracion> crearValoracion(
            @PathVariable Long idCita,
            @RequestBody Valoracion valoracion,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idCliente = userDetails.getId(); // por si lo necesitas en el servicio

        Valoracion creada = servicioValoracion.crearValoracion(valoracion, idCita /* , idCliente */);
        return ResponseEntity.status(201).body(creada);
    }

    // Actualizar una valoración (p.ej. cambiar comentario o foto)
    @PutMapping("/{idValoracion}")
    @PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    public ResponseEntity<Valoracion> actualizarValoracion(
            @PathVariable Long idValoracion,
            @RequestBody Valoracion valoracion) {

        Valoracion actualizada = servicioValoracion.actualizarValoracion(idValoracion, valoracion);
        return ResponseEntity.ok(actualizada);
    }

    // Borrar una valoración
    @DeleteMapping("/{idValoracion}")
    @PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarValoracion(@PathVariable Long idValoracion) {
        servicioValoracion.eliminarValoracion(idValoracion);
        return ResponseEntity.noContent().build();
    }

    // Listar valoraciones de una cita
    @GetMapping("/cita/{idCita}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GRUPO') or hasAuthority('CLIENTE')")
    public ResponseEntity<List<Valoracion>> listarPorCita(@PathVariable Long idCita) {
        return ResponseEntity.ok(servicioValoracion.obtenerPorCita(idCita));
    }

    // Listar todas (solo admin)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Valoracion>> listarTodas() {
        return ResponseEntity.ok(servicioValoracion.obtenerTodas());
    }

    @GetMapping("/servicio/{idServicio}/media")
    public ResponseEntity<Double> mediaPorServicio(@PathVariable Long idServicio) {
        Double media = valoracionRepository.mediaPuntuacionPorServicio(idServicio);
        return ResponseEntity.ok(media != null ? media : 0.0);
    }

    @ExceptionHandler(com.peluqueria.exception.ValoracionException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.peluqueria.advice.ErrorMessage> handleValoracionException(
            com.peluqueria.exception.ValoracionException ex,
            org.springframework.web.context.request.WebRequest request) {
        com.peluqueria.advice.ErrorMessage message = new com.peluqueria.advice.ErrorMessage(
                org.springframework.http.HttpStatus.BAD_REQUEST.value(),
                new java.util.Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

}
