package com.peluqueria.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.peluqueria.entity.Admin;
import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.Grupo;
import com.peluqueria.entity.Usuario;
import com.peluqueria.payload.request.LogInRequest;
import com.peluqueria.payload.response.JwtResponse;
import com.peluqueria.payload.response.MessageResponse;
import com.peluqueria.repository.UsuarioRepository;
import com.peluqueria.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Value("${google.clientId}")
    private String googleClientId;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogInRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
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
                jwt, userDetails.getId(), userDetails.getNombre(),
                userDetails.getApellidos(), userDetails.getUsername(), rol
        ));
    }

    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, Object> data) {
        String email = (String) data.get("email");
        String nombre = (String) data.get("nombre");
        String apellidos = (String) data.get("apellidos");
        String googleId = (String) data.get("googleId");

        // 1. Buscamos al usuario en la BD
        Usuario usuario = usuarioRepository.findByEmail(email);

        // 2. Si no existe, lo creamos como Cliente
        if (usuario == null) {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setEmail(email);
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setApellidos(apellidos != null ? apellidos : "");
            // Generamos un username único para cumplir con el UNIQUE de tu BD
            nuevoCliente.setUsername(email.split("@")[0] + "_" + googleId.substring(0, 4));
            nuevoCliente.setRol("CLIENTE");
            nuevoCliente.setPassword(passwordEncoder.encode("GOOGLE_USER_" + googleId));

            // Al salvar Cliente, JPA rellena ambas tablas (usuarios y cliente)
            usuario = usuarioRepository.save(nuevoCliente);
        }

        // 3. Generamos el token usando TU método 'generarToken'
        // Pasamos el username y el rol como pide tu clase
        String token = jwtUtils.generarToken(usuario.getUsername(), usuario.getRol());

        // 4. Devolvemos la respuesta que espera tu clase JwtResponse
        return ResponseEntity.ok(new JwtResponse(
                token,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellidos()
        ));
    }
    @PostMapping("/signup/admin")
    public ResponseEntity<?> crearAdmin(@Valid @RequestBody Admin admin) {
        if (usuarioRepository.findByUsername(admin.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username en uso!"));
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRol("ADMIN");
        usuarioRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("Admin creado correctamente."));
    }

    @PostMapping("/signup/cliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente) {
        if (usuarioRepository.findByUsername(cliente.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username en uso!"));
        }
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setRol("CLIENTE");
        usuarioRepository.save(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente creado correctamente."));
    }

    @PostMapping("/signup/grupo")
    public ResponseEntity<?> crearGrupo(@Valid @RequestBody Grupo grupo) {
        if (usuarioRepository.findByUsername(grupo.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username en uso!"));
        }
        grupo.setPassword(passwordEncoder.encode(grupo.getPassword()));
        grupo.setRol("GRUPO");
        usuarioRepository.save(grupo);
        return ResponseEntity.ok(new MessageResponse("Grupo creado correctamente."));
    }
}