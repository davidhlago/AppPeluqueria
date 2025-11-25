package com.peluqueria.controllers;

import com.peluqueria.entity.TipoServicio;
import com.peluqueria.security.service.ServicioTipoServicio;
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
@RequestMapping("/api/tipos-servicio")
public class TipoServicioController {

    private final ServicioTipoServicio servicioTipoServicio;

    @Autowired
    public TipoServicioController(ServicioTipoServicio servicioTipoServicio) {
        this.servicioTipoServicio = servicioTipoServicio;
    }

    @GetMapping
    public List<TipoServicio> obtenerTodos() {
        return servicioTipoServicio.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicio> obtenerPorId(@PathVariable Long id) {
        try {
            TipoServicio tipo = servicioTipoServicio.obtenerPorId(id);
            return ResponseEntity.ok(tipo);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TipoServicio> crearTipoServicio(@Valid @RequestBody TipoServicio tipoServicio) {
        TipoServicio nuevo = servicioTipoServicio.guardar(tipoServicio);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TipoServicio> actualizarTipoServicio(@PathVariable Long id, @RequestBody TipoServicio detalles) {
        try {
            TipoServicio existente = servicioTipoServicio.obtenerPorId(id);
            existente.setNombre(detalles.getNombre());
            existente.setDescripcion(detalles.getDescripcion());
            TipoServicio actualizado = servicioTipoServicio.guardar(existente);
            return ResponseEntity.ok(actualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarTipoServicio(@PathVariable Long id) {
        try {
            servicioTipoServicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TipoServicio>> buscarPorTexto(@RequestParam String texto) {
        List<TipoServicio> resultados = servicioTipoServicio.buscarPorTexto(texto);
        return resultados.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultados);
    }
}