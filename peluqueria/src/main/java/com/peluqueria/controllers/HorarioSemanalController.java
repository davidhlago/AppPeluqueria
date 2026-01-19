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

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public HorarioSemanal crear(@RequestBody HorarioSemanal horario) {
        return servicioHorario.guardarHorario(horario);
    }

    @GetMapping("/grupo/{id}")
    public List<HorarioSemanal> porGrupo(@PathVariable Long id) {
        return servicioHorario.obtenerPorGrupo(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        servicioHorario.eliminarHorario(id);
        return ResponseEntity.ok("Horario eliminado");
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public HorarioSemanal actualizar(@PathVariable Long id, @RequestBody HorarioSemanal horarioDetails) {
        HorarioSemanal horario = servicioHorario.obtenerPorId(id);
        horario.setCupoMaximo(horarioDetails.getCupoMaximo());
        horario.setDiasSemana(horarioDetails.getDiasSemana());
        return servicioHorario.guardarHorario(horario);
    }
}