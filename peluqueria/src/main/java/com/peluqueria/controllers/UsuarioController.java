package com.peluqueria.controllers;

import com.peluqueria.entity.Usuario;
import com.peluqueria.service.ServicioUsuario;
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

    // 1. Inyección de dependencia renombrada a 'servicioUsuario'
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public UsuarioController(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    /** GET: Obtener todos los usuarios. Solo para ADMINISTRADORES. */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public List<Usuario> obtenerTodosLosUsuarios() {
        // 2. Usando el método correcto en español
        return servicioUsuario.obtenerTodosLosUsuarios();
    }

    /** GET: Obtener un usuario por ID. ADMIN o el propio usuario. */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    // 3. Retornando la entidad Usuario
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            // 4. Usando el método correcto en español
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** PUT: Actualizar un usuario existente. ADMIN o el propio usuario. */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    // 5. Usando la entidad Usuario como parámetro y tipo de retorno
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario detallesUsuario) {
        try {
            // 6. Usando el método correcto en español
            Usuario updatedUser = servicioUsuario.actualizarUsuario(id, detallesUsuario);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** DELETE: Eliminar un usuario. Solo para ADMINISTRADORES. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            // 7. Usando el método correcto en español
            servicioUsuario.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}