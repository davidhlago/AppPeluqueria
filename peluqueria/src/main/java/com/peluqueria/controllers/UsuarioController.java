package com.peluqueria.controllers;

import com.peluqueria.entity.Usuario;
import com.peluqueria.security.service.ServicioUsuario;
import com.peluqueria.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    // ----------------------------------------------------------------------------------
    // 1. ENDPOINT DEDICADO: /api/usuarios/me (PERFIL DEL USUARIO AUTENTICADO)
    // ----------------------------------------------------------------------------------
    // Devuelve un ÚNICO objeto (ResponseEntity<Usuario>), asegurando la corrección en C#.

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Usuario> obtenerPerfilUsuarioActual(Authentication authentication) {
        // 1. Obtener los detalles del usuario a partir del token JWT
        //    (Spring ya ha verificado el token y ha inyectado el objeto UserDetails).
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 2. Usar el ID (o el username, si lo prefieres) para obtener el objeto completo de la DB.
        Long idUsuario = userDetails.getId();

        try {
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);

            // 🚨 IMPORTANTE: Devolver ResponseEntity.ok(usuario) devuelve UN SOLO OBJETO, no un array.
            // Esto corrige el parseo JSON en tu aplicación C#.
            return ResponseEntity.ok(usuario);
        } catch (NoSuchElementException e) {
            // Esto solo ocurriría si el token es válido pero el usuario fue borrado de la DB.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ----------------------------------------------------------------------------------
    // 2. ENDPOINT /api/usuarios (SOLO PARA ADMINS: LISTA COMPLETA)
    // ----------------------------------------------------------------------------------
    // Se ajusta la lógica para que solo los ADMIN puedan obtener la lista completa,
    // y se elimina la lógica de devolver solo un usuario si no es admin (ya cubierto por /me).

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // Solo ADMINs pueden ver la lista completa
    public List<Usuario> obtenerTodosLosUsuarios() {
        return servicioUsuario.obtenerTodosLosUsuarios();
    }

    // ----------------------------------------------------------------------------------
    // RESTO DE MÉTODOS (Sin cambios)
    // ----------------------------------------------------------------------------------

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario detallesUsuario) {
        try {
            Usuario usuarioActualizado = servicioUsuario.actualizarUsuario(id, detallesUsuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try {
            servicioUsuario.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/buscar/email")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Usuario>> buscarPorEmail(@RequestParam String texto) {
        List<Usuario> usuarios = servicioUsuario.buscarPorEmail(texto);
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }
}