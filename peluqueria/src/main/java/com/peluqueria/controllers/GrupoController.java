package com.peluqueria.controllers;

import com.peluqueria.entity.Grupo;
import com.peluqueria.service.ServicioGrupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/grupos") // Endpoint en español para coherencia
public class GrupoController {

    private final ServicioGrupo servicioGrupo;

    @Autowired
    public GrupoController(ServicioGrupo servicioGrupo) {
        this.servicioGrupo = servicioGrupo;
    }

    /** POST: Permite crear un nuevo grupo (curso y turno). Solo para ADMINISTRADORES. */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Grupo> guardarGrupo(@Valid @RequestBody Grupo grupo) {
        // Se espera un JSON con los campos de la entidad Grupo: {"curso": "...", "turno": "..."}
        Grupo grupoGuardado = servicioGrupo.guardarGrupo(grupo);
        return new ResponseEntity<>(grupoGuardado, HttpStatus.CREATED);
    }

    /** GET: Obtener todos los grupos. Solo para usuarios autenticados. */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Grupo> obtenerTodosLosGrupos() {
        return servicioGrupo.obtenerTodosLosGrupos();
    }

    /** GET: Obtener un grupo por ID. Solo para usuarios autenticados. */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Grupo> obtenerGrupoPorId(@PathVariable Long id) {
        try {
            Grupo grupo = servicioGrupo.obtenerGrupoPorId(id);
            return ResponseEntity.ok(grupo);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** DELETE: Eliminar un grupo. Solo para ADMINISTRADORES. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarGrupo(@PathVariable Long id) {
        try {
            servicioGrupo.eliminarGrupo(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}