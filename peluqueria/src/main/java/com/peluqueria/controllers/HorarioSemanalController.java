package com.peluqueria.controllers;

import com.peluqueria.entity.HorarioSemanal;
import com.peluqueria.security.service.ServicioHorarioSemanal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios-semanales")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HorarioSemanalController {

    @Autowired
    private ServicioHorarioSemanal servicioHorario;

    @GetMapping
    public List<HorarioSemanal> listarTodos() {
        return servicioHorario.obtenerTodos();
    }

    @GetMapping("/grupo/{id}")
    public List<HorarioSemanal> porGrupo(@PathVariable Long id) {
        return servicioHorario.obtenerPorGrupo(id);
    }

    // --- CREAR ---
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> crear(@RequestBody HorarioSemanal horario) {
        try {
            HorarioSemanal creado = servicioHorario.guardarHorario(horario);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- ACTUALIZAR ---
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody HorarioSemanal horarioDetails) {
        try {
            HorarioSemanal actualizado = servicioHorario.actualizarHorario(id, horarioDetails);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- ELIMINAR ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            servicioHorario.eliminarHorario(id);
            return ResponseEntity.ok("Horario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}