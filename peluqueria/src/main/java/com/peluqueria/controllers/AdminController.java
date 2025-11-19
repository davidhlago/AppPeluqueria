package com.peluqueria.controllers;

import com.peluqueria.entity.Admin;
import com.peluqueria.service.ServicioAdmin;
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
@RequestMapping("/api/admins")
public class AdminController {

    private final ServicioAdmin servicioAdmin;

    @Autowired
    public AdminController(ServicioAdmin servicioAdmin) {
        this.servicioAdmin = servicioAdmin;
    }

    /** POST: Crear un nuevo admin. Solo ADMINISTRADOR. */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Admin> guardarAdmin(@Valid @RequestBody Admin admin) {
        Admin adminGuardado = servicioAdmin.guardarAdmin(admin);
        return new ResponseEntity<>(adminGuardado, HttpStatus.CREATED);
    }

    /** GET: Listar todos los admins. */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public List<Admin> obtenerTodosLosAdmins() {
        return servicioAdmin.obtenerTodosLosAdmins();
    }

    /** GET: Obtener admin por ID. */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Admin> obtenerAdminPorId(@PathVariable Long id) {
        try {
            Admin admin = servicioAdmin.obtenerAdminPorId(id);
            return ResponseEntity.ok(admin);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** DELETE: Eliminar admin. Solo ADMINISTRADOR. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarAdmin(@PathVariable Long id) {
        try {
            servicioAdmin.eliminarAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
