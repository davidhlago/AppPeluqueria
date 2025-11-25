package com.peluqueria.controllers;

import com.peluqueria.entity.Cliente;
import com.peluqueria.security.service.ServicioCliente;
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

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> obtenerTodosLosClientes() {
        return servicioCliente.obtenerTodosLosClientes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        try {
            Cliente cliente = servicioCliente.obtenerClientePorId(id);
            return ResponseEntity.ok(cliente);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente detallesCliente) {
        try {
            Cliente clienteActualizado = servicioCliente.actualizarCliente(id, detallesCliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        try {
            servicioCliente.eliminarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Cliente>> buscarClientes(@RequestParam String texto) {
        List<Cliente> clientes = servicioCliente.buscarObservacionesOAlergenos(texto);
        return clientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientes);
    }
}