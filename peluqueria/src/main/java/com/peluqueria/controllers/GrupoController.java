package com.peluqueria.controllers;

import com.peluqueria.entity.Grupo;
import com.peluqueria.security.service.ServicioGrupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final ServicioGrupo servicioGrupo;

    @Autowired
    public GrupoController(ServicioGrupo servicioGrupo) {
        this.servicioGrupo = servicioGrupo;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('GRUPO')")
    public List<Grupo> obtenerTodosLosGrupos() {
        return servicioGrupo.obtenerTodosLosGrupos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Grupo> obtenerGrupoPorId(@PathVariable Long id) {
        try {
            Grupo grupo = servicioGrupo.obtenerGrupoPorId(id);
            return ResponseEntity.ok(grupo);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Grupo> actualizarGrupo(@PathVariable Long id, @RequestBody Grupo detallesGrupo) {
        try {
            Grupo grupoExistente = servicioGrupo.obtenerGrupoPorId(id);
            grupoExistente.setNombre(detallesGrupo.getNombre());
            grupoExistente.setApellidos(detallesGrupo.getApellidos());
            grupoExistente.setEmail(detallesGrupo.getEmail());
            grupoExistente.setCurso(detallesGrupo.getCurso());
            grupoExistente.setTurno(detallesGrupo.getTurno());
            Grupo grupoActualizado = servicioGrupo.guardarGrupo(grupoExistente);
            return ResponseEntity.ok(grupoActualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarGrupo(@PathVariable Long id) {
        try {
            servicioGrupo.eliminarGrupo(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Grupo>> buscarGrupos(@RequestParam String texto) {
        List<Grupo> grupos = servicioGrupo.buscarPorCurso(texto);
        return grupos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(grupos);
    }
}