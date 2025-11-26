package com.peluqueria.controllers;

import com.peluqueria.entity.Admin;
import com.peluqueria.security.service.ServicioAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final ServicioAdmin servicioAdmin;

    @Autowired
    public AdminController(ServicioAdmin servicioAdmin) {
        this.servicioAdmin = servicioAdmin;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Admin> obtenerTodosLosAdmins() {
        return servicioAdmin.obtenerTodosLosAdmins();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Admin> obtenerAdminPorId(@PathVariable Long id) {
        try {
            Admin admin = servicioAdmin.obtenerAdminPorId(id);
            return ResponseEntity.ok(admin);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Admin> actualizarAdmin(@PathVariable Long id, @RequestBody Admin adminDetalles) {
        try {
            Admin adminExistente = servicioAdmin.obtenerAdminPorId(id);
            adminExistente.setNombre(adminDetalles.getNombre());
            adminExistente.setApellidos(adminDetalles.getApellidos());
            adminExistente.setEmail(adminDetalles.getEmail());
            adminExistente.setEspecialidad(adminDetalles.getEspecialidad());

            Admin adminActualizado = servicioAdmin.guardarAdmin(adminExistente);
            return ResponseEntity.ok(adminActualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarAdmin(@PathVariable Long id) {
        try {
            servicioAdmin.eliminarAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Admin>> buscarPorEspecialidad(@RequestParam String texto) {
        List<Admin> admins = servicioAdmin.buscarPorEspecialidad(texto);
        return admins.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(admins);
    }
}