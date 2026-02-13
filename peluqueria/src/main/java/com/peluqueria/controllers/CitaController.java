package com.peluqueria.controllers;

import com.peluqueria.entity.Cita;
import com.peluqueria.exception.CitaException;
import com.peluqueria.security.service.ServicioCita;
import com.peluqueria.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CitaController {

    @Autowired
    private ServicioCita citaService;

    // --- MÉTODOS DE LECTURA ---

    @GetMapping("/todas")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GRUPO')")
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.obtenerTodas());
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> listarCitasCliente(@PathVariable Long clienteId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long idLogueado = userDetails.getId();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        // VALIDACIÓN DE SEGURIDAD: Un cliente solo ve lo suyo
        if (!isAdmin && !idLogueado.equals(clienteId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\": \"No tienes permiso para ver las citas de otro cliente.\"}");
        }

        return ResponseEntity.ok(citaService.obtenerPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCitaById(@PathVariable long id, Authentication authentication) {
        try {
            Cita cita = citaService.obtenerPorId(id);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long idLogueado = userDetails.getId();

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));

            // VALIDACIÓN: Si no es admin y la cita no le pertenece, 403
            if (!isAdmin && !cita.getCliente().getId().equals(idLogueado)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("{\"error\": \"Acceso denegado a esta cita.\"}");
            }

            return ResponseEntity.ok(cita);
        } catch (CitaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/huecos")
    public ResponseEntity<?> getHuecosPorServicio(
            @RequestParam(name = "fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(name = "idServicio") Long idServicio) {
        return ResponseEntity.ok(citaService.obtenerHuecosPorServicioYFecha(idServicio, fecha));
    }

    // --- MÉTODOS DE ESCRITURA ---

    @PostMapping("/reservar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> addCita(@RequestBody Cita cita) {
        Cita added = citaService.crearCita(cita);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> cancelarCitaPorCliente(@PathVariable Long id) {
        try {
            citaService.cancelarCita(id);
            return ResponseEntity.ok("{\"mensaje\": \"Cita cancelada correctamente\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo cancelar: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado/{opcion}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GRUPO')")
    public ResponseEntity<?> cambiarEstado(@PathVariable long id, @PathVariable int opcion) {
        try {
            Cita citaActualizada = citaService.gestionarEstadoCita(id, opcion);
            return ResponseEntity.ok(citaActualizada);
        } catch (CitaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> deleteCita(@PathVariable long id) {
        try {
            citaService.cancelarCita(id);
            return ResponseEntity.noContent().build();
        } catch (CitaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/dias-disponibles")
    public ResponseEntity<List<Integer>> getDiasLaborables(@RequestParam Long idServicio) {
        return ResponseEntity.ok(citaService.obtenerDiasLaborablesPorServicio(idServicio));
    }

    // --- MANEJO DE EXCEPCIONES ---

    @ExceptionHandler(com.peluqueria.exception.HorarioException.class)
    public ResponseEntity<?> handleHorarioException(com.peluqueria.exception.HorarioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(com.peluqueria.exception.CitaException.class)
    public ResponseEntity<?> handleCitaException(com.peluqueria.exception.CitaException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}