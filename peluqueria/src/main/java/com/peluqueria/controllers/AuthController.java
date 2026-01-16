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
// 👇 IMPORTANTE: Importa tu servicio correctamente
import com.peluqueria.security.service.ServicioEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

    // 👇 CORRECCIÓN: El tipo debe ser la clase 'ServicioEmail'
    @Autowired
    private ServicioEmail emailService;

    @Value("${google.clientId}")
    private String googleClientId;

    // ---------------- LOGIN NORMAL ----------------
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

    // ---------------- LOGIN CON GOOGLE ----------------
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, Object> data) {
        System.out.println("🚀 Backend: Petición Google recibida");

        try {
            String idTokenString = (String) data.get("idToken");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String nombre = (String) payload.get("name");
            String googleId = payload.getSubject();

            Usuario usuario = usuarioRepository.findByEmail(email);

            if (usuario == null) {
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setEmail(email);
                nuevoCliente.setNombre(nombre);
                nuevoCliente.setApellidos("");
                nuevoCliente.setUsername(email.split("@")[0] + "_" + googleId.substring(0, 4));
                nuevoCliente.setRol("CLIENTE");
                nuevoCliente.setPassword(passwordEncoder.encode("GOOGLE_USER_" + UUID.randomUUID().toString()));

                usuario = usuarioRepository.save(nuevoCliente);
            }

            String token = jwtUtils.generarToken(usuario.getUsername(), usuario.getRol());

            return ResponseEntity.ok(new JwtResponse(
                    token,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidos() != null ? usuario.getApellidos() : "",
                    usuario.getUsername(),
                    usuario.getRol()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error en autenticación Google: " + e.getMessage()));
        }
    }

    // ====================================================================
    // 👇👇👇 ZONA DE RECUPERACIÓN DE CONTRASEÑA 👇👇👇
    // ====================================================================

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Email no encontrado");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        usuario.setResetToken(code);
        usuarioRepository.save(usuario);

        // 👇 CORRECCIÓN: Usamos la variable 'emailService', NO la clase estática
        try {
            emailService.enviarCodigoRecuperacion(email, code);
            return ResponseEntity.ok("{\"mensaje\": \"Código enviado a tu correo\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        if (usuario.getResetToken() == null || !usuario.getResetToken().equals(code)) {
            return ResponseEntity.badRequest().body("Código inválido o expirado");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setResetToken(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("{\"mensaje\": \"Contraseña actualizada correctamente\"}");
    }

    // ====================================================================
    // ---------------- SIGNUP METHODS ----------------
    // ====================================================================

    @PostMapping("/signup/admin")
    public ResponseEntity<?> crearAdmin(@Valid @RequestBody Admin admin) {
        if (usuarioRepository.findByUsername(admin.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario en uso."));
        }
        if (usuarioRepository.findByEmail(admin.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email en uso."));
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRol("ADMIN");
        usuarioRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("Admin creado correctamente."));
    }

    @PostMapping("/signup/cliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente) {
        if (usuarioRepository.findByUsername(cliente.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario en uso."));
        }
        if (usuarioRepository.findByEmail(cliente.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email en uso."));
        }
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setRol("CLIENTE");
        usuarioRepository.save(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente creado correctamente."));
    }

    @PostMapping("/signup/grupo")
    public ResponseEntity<?> crearGrupo(@Valid @RequestBody Grupo grupo) {
        if (usuarioRepository.findByUsername(grupo.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario en uso."));
        }
        if (usuarioRepository.findByEmail(grupo.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email en uso."));
        }
        grupo.setPassword(passwordEncoder.encode(grupo.getPassword()));
        grupo.setRol("GRUPO");
        usuarioRepository.save(grupo);
        return ResponseEntity.ok(new MessageResponse("Grupo creado correctamente."));
    }

    @PostMapping("/signup/mobile")
    public ResponseEntity<?> crearClienteDesdeMovil(@Valid @RequestBody Cliente cliente) {
        if (usuarioRepository.findByUsername(cliente.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario en uso."));
        }
        if (usuarioRepository.findByEmail(cliente.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email en uso."));
        }
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        cliente.setRol("CLIENTE");
        usuarioRepository.save(cliente);
        return ResponseEntity.ok(new MessageResponse("Cliente creado correctamente."));
    }
}