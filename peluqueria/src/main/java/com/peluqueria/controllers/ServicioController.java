package com.peluqueria.controllers;

import com.peluqueria.entity.Servicio;
import com.peluqueria.entity.TipoServicio;
import com.peluqueria.security.service.ServicioService;
import com.peluqueria.repository.TipoServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    // ============================
    // LISTAR TODOS
    // ============================
    @GetMapping
    public ResponseEntity<List<Servicio>> listarServicios() {
        List<Servicio> servicios = servicioService.findAll();
        return servicios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(servicios);
    }

    // ============================
    // OBTENER POR ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerServicio(@PathVariable Long id) {
        Servicio servicio = servicioService.findById(id);
        return (servicio != null) ? ResponseEntity.ok(servicio) : ResponseEntity.notFound().build();
    }

    // ============================
    // CREAR SERVICIO
    // ============================
    @PostMapping
    public ResponseEntity<Servicio> crearServicio(@RequestBody Servicio servicio) {
        if (servicio.getTipoServicioId() != null) {
            TipoServicio tipo = tipoServicioRepository.findById(servicio.getTipoServicioId())
                    .orElseThrow(() -> new RuntimeException("TipoServicio no encontrado"));
            servicio.setTipoServicio(tipo);
        }
        Servicio nuevo = servicioService.save(servicio);
        return ResponseEntity.status(201).body(nuevo);
    }

    // ============================
    // ACTUALIZAR SERVICIO
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicio) {
        Servicio existente = servicioService.findById(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        existente.setNombre(servicio.getNombre());
        existente.setDescripcion(servicio.getDescripcion());
        existente.setDuracionBloques(servicio.getDuracionBloques());
        existente.setPrecio(servicio.getPrecio());

        if (servicio.getTipoServicioId() != null) {
            TipoServicio tipo = tipoServicioRepository.findById(servicio.getTipoServicioId())
                    .orElseThrow(() -> new RuntimeException("TipoServicio no encontrado"));
            existente.setTipoServicio(tipo);
        }

        Servicio actualizado = servicioService.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    // ============================
    // ELIMINAR SERVICIO
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        Servicio servicio = servicioService.findById(id);
        if (servicio == null) {
            return ResponseEntity.notFound().build();
        }
        servicioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ============================
    // BUSCAR POR NOMBRE
    // ============================
    @GetMapping("/buscar")
    public ResponseEntity<List<Servicio>> buscarPorNombre(@RequestParam String nombre) {
        List<Servicio> resultados = servicioService.buscarPorNombre(nombre);
        return resultados.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultados);
    }

    // ============================
    // BUSCAR POR DURACIÓN
    // ============================
    @GetMapping("/buscar/duracion")
    public ResponseEntity<List<Servicio>> buscarPorDuracion(@RequestParam int duracion) {
        List<Servicio> resultados = servicioService.buscarPorDuracionMinimaNativa(duracion);
        return resultados.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultados);
    }
}
