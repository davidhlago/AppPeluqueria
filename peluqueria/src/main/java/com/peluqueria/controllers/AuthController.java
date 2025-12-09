package com.peluqueria.controllers;

import com.peluqueria.entity.Admin;
import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.Grupo;
import com.peluqueria.payload.request.LogInRequest;
import com.peluqueria.payload.response.JwtResponse;
import com.peluqueria.payload.response.MessageResponse;
import com.peluqueria.repository.UsuarioRepository;
import com.peluqueria.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // ---------------- LOGIN ----------------
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogInRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            com.peluqueria.security.service.UserDetailsImpl userDetails =
                    (com.peluqueria.security.service.UserDetailsImpl) authentication.getPrincipal();

            String rol = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority())
                    .orElse(null);

            String jwt = jwtUtils.generarToken(userDetails.getUsername(), rol);

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getNombre(),
                    userDetails.getApellidos(),
                    userDetails.getUsername(),
                    rol
            ));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401)
                    .body(new MessageResponse("Error: El usuario o contraseña introducidos son incorrectos."));
        }
    }

    // ---------------- SIGNUP ADMIN ----------------
    @PostMapping("/signup/admin")
    public ResponseEntity<?> crearAdmin(@Valid @RequestBody Admin admin) {
        if (usuarioRepository.findByUsername(admin.getUsername()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario introducido ya está en uso."));
        }
        if (usuarioRepository.findByEmail(admin.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El correo introducido ya está en uso."));
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRol("ADMIN");
        usuarioRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("Admin creado correctamente."));
    }

    // ---------------- SIGNUP CLIENTE ----------------
    @PostMapping("/signup/cliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente) {
        if (usuarioRepository.findByUsername(cliente.getUsername()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario introducido ya está en uso."));
        }
        if (usuarioRepository.findByEmail(cliente.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El correo introducido ya está en uso."));
        }
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setRol("CLIENTE");
        usuarioRepository.save(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente creado correctamente."));
    }

    // ---------------- SIGNUP GRUPO ----------------
    @PostMapping("/signup/grupo")
    public ResponseEntity<?> crearGrupo(@Valid @RequestBody Grupo grupo) {
        if (usuarioRepository.findByUsername(grupo.getUsername()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario introducido ya está en uso."));
        }
        if (usuarioRepository.findByEmail(grupo.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El correo introducido ya está en uso."));
        }
        grupo.setPassword(passwordEncoder.encode(grupo.getPassword()));
        grupo.setRol("GRUPO");
        usuarioRepository.save(grupo);
        return ResponseEntity.ok(new MessageResponse("Grupo creado correctamente."));
    }

    // ---------------- SIGNUP MOBILE ----------------
    @PostMapping("/signup/mobile")
    public ResponseEntity<?> crearClienteDesdeMovil(@Valid @RequestBody Cliente cliente) {
        if (usuarioRepository.findByUsername(cliente.getUsername()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El nombre de usuario introducido ya está en uso."));
        }
        if (usuarioRepository.findByEmail(cliente.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El correo introducido ya está en uso."));
        }
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setRol("CLIENTE"); // Forzamos siempre CLIENTE
        usuarioRepository.save(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente creado correctamente desde móvil."));
    }
}
