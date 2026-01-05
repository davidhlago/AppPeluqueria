package com.peluqueria.controllers;

import com.peluqueria.entity.Cita;
import com.peluqueria.security.service.ServicioCita;
import com.peluqueria.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CitaController {

    @Autowired
    private ServicioCita servicioCita;

    // --- LISTAR CITAS (Según rol) ---
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Cita> listarCitas(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();

        if ("ADMIN".equals(rol)) {
            return servicioCita.obtenerTodas();
        } else if ("GRUPO".equals(rol)) {
            return servicioCita.obtenerPorGrupo(userDetails.getId());
        } else {
            return servicioCita.obtenerPorCliente(userDetails.getId());
        }
    }

    // --- CREAR CITA ---
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> guardarCita(@RequestBody Cita cita) {
        try {
            Cita nuevaCita = servicioCita.crearCita(cita);
            return ResponseEntity.ok(nuevaCita);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- EDITAR CITA (NUEVO) ---
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> editarCita(@PathVariable Long id, @RequestBody Cita cita) {
        try {
            Cita citaEditada = servicioCita.modificarCita(id, cita);
            return ResponseEntity.ok(citaEditada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- CANCELAR CITA ---
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        servicioCita.cancelarCita(id);
        return ResponseEntity.ok("Cita cancelada correctamente");
    }

    // --- VER HUECOS LIBRES ---
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> verHuecos(@RequestParam String fecha,
                                       @RequestParam Long servicioId,
                                       @RequestParam Long grupoId) {
        try {
            LocalDate fechaL = LocalDate.parse(fecha);
            List<LocalTime> huecos = servicioCita.obtenerHuecosDisponibles(fechaL, servicioId, grupoId);
            return ResponseEntity.ok(huecos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}