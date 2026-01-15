package com.peluqueria.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.peluqueria.entity.Admin;
import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.Grupo;
import com.peluqueria.entity.Usuario; // Asegúrate de importar tu clase padre Usuario
import com.peluqueria.payload.request.LogInRequest;
import com.peluqueria.payload.response.JwtResponse;
import com.peluqueria.payload.response.MessageResponse;
import com.peluqueria.repository.UsuarioRepository;
import com.peluqueria.security.jwt.JwtUtils;
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

    // Inyectamos el ID de cliente desde application.properties
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

    // ---------------- LOGIN CON GOOGLE (NUEVO) ----------------
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, Object> data) {
        System.out.println("🚀 Backend: Petición Google recibida");

        try {
            String idTokenString = (String) data.get("idToken");

            // 1. Verificamos el token con Google para seguridad
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                System.out.println("❌ Backend: Token Google inválido.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            // 2. Extraemos datos del token verificado
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String nombre = (String) payload.get("name");
            String googleId = payload.getSubject();

            System.out.println("✅ Token verificado. Email: " + email);

            // 3. Buscamos si el usuario ya existe
            Usuario usuario = usuarioRepository.findByEmail(email);

            if (usuario == null) {
                System.out.println("🆕 Usuario nuevo detectado. Registrando...");
                // Si no existe, creamos un CLIENTE nuevo
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setEmail(email);
                nuevoCliente.setNombre(nombre);
                nuevoCliente.setApellidos(""); // Google a veces no manda apellidos separados
                // Generamos un username único basado en el email
                nuevoCliente.setUsername(email.split("@")[0] + "_" + googleId.substring(0, 4));
                nuevoCliente.setRol("CLIENTE");
                // Contraseña dummy (no se usará porque entra por Google)
                nuevoCliente.setPassword(passwordEncoder.encode("GOOGLE_USER_" + UUID.randomUUID().toString()));

                usuario = usuarioRepository.save(nuevoCliente);
            }

            // 4. Generamos el JWT propio de tu sistema
            String token = jwtUtils.generarToken(usuario.getUsername(), usuario.getRol());

            // 5. Devolvemos la respuesta exacta que espera tu App Flutter
            return ResponseEntity.ok(new JwtResponse(
                    token,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidos() != null ? usuario.getApellidos() : "",
                    usuario.getUsername(),
                    usuario.getRol()
            ));

        } catch (Exception e) {
            System.out.println("❌ Error en AuthController Google: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error en autenticación Google: " + e.getMessage()));
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