package com.peluqueria.controllers;

import com.peluqueria.entity.BloqueoHorario;
import com.peluqueria.security.service.ServicioBloqueoHorario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/bloqueos-horarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BloqueoHorarioController {

    @Autowired
    private ServicioBloqueoHorario servicioBloqueo;

    // GET - Listar todos los bloqueos
    @GetMapping
    public List<BloqueoHorario> listarTodos() {
        return servicioBloqueo.listarTodos();
    }

    // GET - Listar por fecha específica
    @GetMapping("/fecha/{fecha}")
    public List<BloqueoHorario> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return servicioBloqueo.listarPorFecha(fecha);
    }

    // GET - Listar por rango de fechas
    @GetMapping("/rango")
    public List<BloqueoHorario> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return servicioBloqueo.listarPorRangoFechas(fechaInicio, fechaFin);
    }

    // GET - Listar por grupo
    @GetMapping("/grupo/{idGrupo}")
    public List<BloqueoHorario> listarPorGrupo(@PathVariable Long idGrupo) {
        return servicioBloqueo.listarPorGrupo(idGrupo);
    }

    // GET - Listar por servicio
    @GetMapping("/servicio/{idServicio}")
    public List<BloqueoHorario> listarPorServicio(@PathVariable Long idServicio) {
        return servicioBloqueo.listarPorServicio(idServicio);
    }

    // GET - Obtener un bloqueo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            BloqueoHorario bloqueo = servicioBloqueo.obtenerPorId(id);
            return ResponseEntity.ok(bloqueo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - Verificar si existe conflicto
    @GetMapping("/verificar-conflicto")
    public ResponseEntity<Boolean> verificarConflicto(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long idGrupo,
            @RequestParam(required = false) Long idServicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin) {
        boolean existe = servicioBloqueo.existeConflicto(fecha, idGrupo, idServicio, horaInicio, horaFin);
        return ResponseEntity.ok(existe);
    }

    // POST - Crear nuevo bloqueo (solo ADMIN)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> crearBloqueo(@RequestBody BloqueoHorario bloqueo) {
        try {
            BloqueoHorario nuevo = servicioBloqueo.crearBloqueo(bloqueo);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE - Eliminar bloqueo (solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminarBloqueo(@PathVariable Long id) {
        try {
            servicioBloqueo.eliminarBloqueo(id);
            return ResponseEntity.ok("Bloqueo eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //editar
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> actualizarBloqueo(@PathVariable Long id, @RequestBody BloqueoHorario bloqueoDetails) {
        try {
            BloqueoHorario actualizado = servicioBloqueo.actualizarBloqueo(id, bloqueoDetails);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
