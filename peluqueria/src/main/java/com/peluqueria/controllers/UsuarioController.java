package com.peluqueria.controllers;

import com.peluqueria.entity.Usuario;
import com.peluqueria.security.service.ServicioUsuario;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public UsuarioController(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    /** GET: Listar todos los usuarios. Solo ADMINISTRADOR. */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public List<Usuario> obtenerTodosLosUsuarios() {
        return servicioUsuario.obtenerTodosLosUsuarios();
    }

    /** GET: Obtener usuario por ID. ADMIN o el propio usuario. */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** PUT: Actualizar usuario. ADMIN o el propio usuario. */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario detallesUsuario) {
        try {
            Usuario usuarioActualizado = servicioUsuario.actualizarUsuario(id, detallesUsuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** DELETE: Eliminar usuario. Solo ADMINISTRADOR. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            servicioUsuario.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
