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

    // Métodos de lectura (GET) - Generalmente seguros, pero podemos capturar si ID no existe

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

    @GetMapping("/disponible")
    public ResponseEntity<?> getCitasDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long horarioId) {
        return ResponseEntity.ok(citaService.obtenerHuecosDisponibles(fecha, horarioId, null));
    }

    // --- Métodos que lanzan excepciones de negocio ---

    @PostMapping("/reservar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> addCita(@RequestBody Cita cita) {
        try {
            Cita added = citaService.crearCita(cita);
            return new ResponseEntity<>(added, HttpStatus.CREATED);
        } catch (HorarioException | CitaException e) {
            // Error controlado (400 Bad Request)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Error inesperado (500 Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado/{opcion}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GRUPO')")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable long id,
            @PathVariable int opcion) {
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
    public ResponseEntity<?> editarCita(
            @PathVariable Long id,
            @RequestBody Cita cita) {
        try {
            Cita citaEditada = citaService.modificarCita(id, cita);
            return ResponseEntity.ok(citaEditada);
        } catch (CitaException | HorarioException e) {
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
}