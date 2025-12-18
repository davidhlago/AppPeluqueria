package com.peluqueria.controllers;

import com.peluqueria.entity.Cita;
import com.peluqueria.security.service.ServicioCita;
import com.peluqueria.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CitaController {

    @Autowired
    private ServicioCita servicioCita;

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

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<Cita> guardarCita(@RequestBody Cita cita) {
        return ResponseEntity.ok(servicioCita.crearCita(cita));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENTE')")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        servicioCita.cancelarCita(id);
        return ResponseEntity.ok("Cita cancelada correctamente");
    }
}