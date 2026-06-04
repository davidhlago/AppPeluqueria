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
import com.peluqueria.security.service.ServicioEmail;
import com.peluqueria.security.service.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Inyección por constructor para evitar fallos de inicialización del Bean
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ServicioEmail emailService;

    @Value("${google.clientId}")
    private String googleClientId;

    public AuthController(AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            ServicioEmail emailService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    // ---------------- LOG IN ----------------
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogInRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String rol = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority())
                    .orElse("CLIENTE");

            String jwt = jwtUtils.generarToken(userDetails.getUsername(), rol);

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getNombre(),
                    userDetails.getApellidos(),
                    userDetails.getUsername(),
                    rol));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401)
                    .body(new MessageResponse("Error: El usuario o contraseña introducidos son incorrectos."));
        }
    }

    // ---------------- RECUPERAR CONTRASEÑA ----------------
    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("idToken");
            if (token == null || token.isBlank()) {
                token = request.get("token");
            }
            if (token == null || token.isBlank()) {
                token = request.get("tokenId");
            }

            if (token == null || token.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Error: Token de Google no enviado."));
            }

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(token);
            if (googleIdToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Error: Token de Google inválido."));
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String nombreGoogle = request.getOrDefault("nombre", (String) payload.get("name"));
            String apellidosGoogle = request.getOrDefault("apellidos", "");

            if (email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Error: No se pudo obtener email desde Google."));
            }

            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario == null) {
                Cliente cliente = new Cliente();
                cliente.setEmail(email);
                cliente.setNombre((nombreGoogle == null || nombreGoogle.isBlank()) ? "Usuario Google" : nombreGoogle);
                cliente.setApellidos(apellidosGoogle == null ? "" : apellidosGoogle);
                cliente.setRol("CLIENTE");
                cliente.setUsername(generarUsernameUnico(email));
                cliente.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

                usuario = usuarioRepository.save(cliente);
            }

            String rol = (usuario.getRol() == null || usuario.getRol().isBlank()) ? "CLIENTE" : usuario.getRol();
            String jwt = jwtUtils.generarToken(usuario.getUsername(), rol);

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidos(),
                    usuario.getUsername(),
                    rol));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error validando Google Sign-In: " + e.getMessage()));
        }
    }

    private String generarUsernameUnico(String email) {
        String base = email.split("@")[0].replaceAll("[^a-zA-Z0-9._-]", "");
        if (base.isBlank()) {
            base = "usuario_google";
        }

        String username = base;
        int i = 1;
        while (usuarioRepository.findByUsername(username) != null) {
            username = base + "_" + i;
            i++;
        }
        return username;
    }

    // ---------------- RECUPERAR CONTRASEÑA ----------------
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

        if (usuario == null || usuario.getResetToken() == null || !usuario.getResetToken().equals(code)) {
            return ResponseEntity.badRequest().body("Usuario no encontrado o código inválido");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setResetToken(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("{\"mensaje\": \"Contraseña actualizada correctamente\"}");
    }

    // ---------------- SIGN UP (REGISTRO) ----------------
    @PostMapping("/signup/cliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente) {
        System.out.println("Solicitud de registro de cliente: " + cliente.getUsername());
        if (usuarioRepository.findByUsername(cliente.getUsername()) != null) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error: El nombre de usuario '" + cliente.getUsername() + "' ya está en uso."));
        }
        if (usuarioRepository.findByEmail(cliente.getEmail()) != null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: El email '" + cliente.getEmail() + "' ya está registrado."));
        }
        try {
            cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
            cliente.setRol("CLIENTE");
            usuarioRepository.save(cliente);
            System.out.println("Cliente registrado con éxito: " + cliente.getUsername());
            return ResponseEntity.ok(new MessageResponse("Cliente registrado correctamente."));
        } catch (Exception e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error interno al registrar cliente: " + e.getMessage()));
        }
    }

    @PostMapping("/signup/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> crearAdmin(@Valid @RequestBody Admin admin) {
        if (usuarioRepository.findByUsername(admin.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario en uso."));
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRol("ADMIN");
        usuarioRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("Administrador registrado correctamente."));
    }

    @PostMapping("/signup/grupo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> crearGrupo(@Valid @RequestBody Grupo grupo) {
        if (usuarioRepository.findByUsername(grupo.getUsername()) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Usuario en uso."));
        }
        grupo.setPassword(passwordEncoder.encode(grupo.getPassword()));
        grupo.setRol("GRUPO");
        usuarioRepository.save(grupo);
        return ResponseEntity.ok(new MessageResponse("Grupo registrado correctamente."));
    }
}