package com.peluqueria.controllers;

import com.peluqueria.entity.Cliente;
import com.peluqueria.service.ServicioCliente;
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
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ServicioCliente servicioCliente;

    @Autowired
    public ClienteController(ServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
    }

    /** POST: Crear un nuevo cliente. Público (registro). */
    @PostMapping
    public ResponseEntity<Cliente> guardarCliente(@Valid @RequestBody Cliente cliente) {
        Cliente clienteGuardado = servicioCliente.guardarCliente(cliente);
        return new ResponseEntity<>(clienteGuardado, HttpStatus.CREATED);
    }

    /** GET: Listar todos los clientes. Solo ADMINISTRADOR. */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public List<Cliente> obtenerTodosLosClientes() {
        return servicioCliente.obtenerTodosLosClientes();
    }

    /** GET: Obtener cliente por ID. ADMIN o el propio cliente. */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = servicioCliente.obtenerClientePorId(id);
            return ResponseEntity.ok(cliente);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** PUT: Actualizar cliente. ADMIN o el propio cliente. */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente detallesCliente) {
        try {
            Cliente clienteActualizado = servicioCliente.actualizarCliente(id, detallesCliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** DELETE: Eliminar cliente. Solo ADMINISTRADOR. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        try {
            servicioCliente.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
