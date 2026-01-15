package com.peluqueria.controllers;

import com.peluqueria.entity.Servicio;
import com.peluqueria.security.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// CORRECCIÓN AQUÍ: Añadimos "/api" para que coincida con SecurityConfig
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public ResponseEntity<List<Servicio>> listarServicios() {
        List<Servicio> servicios = servicioService.findAll();
        return servicios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerServicio(@PathVariable Long id) {
        Servicio servicio = servicioService.findById(id);
        return (servicio != null) ? ResponseEntity.ok(servicio) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Servicio> crearServicio(@RequestBody Servicio servicio) {
        Servicio nuevo = servicioService.save(servicio);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicioDetalles) {
        Servicio existente = servicioService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setNombre(servicioDetalles.getNombre());
        existente.setDescripcion(servicioDetalles.getDescripcion());
        existente.setDuracionBloques(servicioDetalles.getDuracionBloques());
        existente.setPrecio(servicioDetalles.getPrecio());
        existente.setTipoServicio(servicioDetalles.getTipoServicio());

        if (servicioDetalles.getImagenBase64() != null && !servicioDetalles.getImagenBase64().isEmpty()) {
            existente.setImagenBase64(servicioDetalles.getImagenBase64());
        }

        Servicio actualizado = servicioService.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        Servicio servicio = servicioService.findById(id);
        if (servicio == null) {
            return ResponseEntity.notFound().build();
        }
        servicioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Servicio>> buscarPorNombre(@RequestParam String nombre) {
        List<Servicio> resultados = servicioService.buscarPorNombre(nombre);
        return resultados.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscar/duracion")
    public ResponseEntity<List<Servicio>> buscarPorDuracion(@RequestParam int duracion) {
        List<Servicio> resultados = servicioService.buscarPorDuracionMinimaNativa(duracion);
        return resultados.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultados);
    }
}