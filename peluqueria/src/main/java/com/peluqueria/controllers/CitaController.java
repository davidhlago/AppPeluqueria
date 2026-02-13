package com.peluqueria.controllers;

import com.peluqueria.entity.Cita;
import com.peluqueria.exception.CitaException;
import com.peluqueria.exception.HorarioException;
import com.peluqueria.security.service.ServicioCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<Cita>> listarCitasCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(citaService.obtenerPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCitaById(@PathVariable long id) {
        try {
            Cita cita = citaService.obtenerPorId(id);
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

    // --- MÉTODOS DE ESCRITURA (POST/PUT/DELETE) ---

    @PostMapping("/reservar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> addCita(@RequestBody Cita cita) {
        Cita added = citaService.crearCita(cita);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    // ✅ MÉTODO ESPECÍFICO PARA CANCELAR DESDE LA APP (PUT)
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> editarCita(@PathVariable Long id, @RequestBody Cita cita) {
        Cita actualizado = citaService.modificarCita(id, cita);
        return ResponseEntity.ok(actualizado);
    }

    @ExceptionHandler(com.peluqueria.exception.HorarioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.peluqueria.advice.ErrorMessage> handleHorarioException(
            com.peluqueria.exception.HorarioException ex, org.springframework.web.context.request.WebRequest request) {
        com.peluqueria.advice.ErrorMessage message = new com.peluqueria.advice.ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new java.util.Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.peluqueria.exception.CitaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.peluqueria.advice.ErrorMessage> handleCitaException(
            com.peluqueria.exception.CitaException ex, org.springframework.web.context.request.WebRequest request) {
        com.peluqueria.advice.ErrorMessage message = new com.peluqueria.advice.ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new java.util.Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.peluqueria.exception.BloqueoHorarioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.peluqueria.advice.ErrorMessage> handleBloqueoHorarioException(
            com.peluqueria.exception.BloqueoHorarioException ex,
            org.springframework.web.context.request.WebRequest request) {
        com.peluqueria.advice.ErrorMessage message = new com.peluqueria.advice.ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new java.util.Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
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
}