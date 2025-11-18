/**package com.peluqueria.controllers;

import com.peluqueria.entity.Usuario;
import com.peluqueria.payload.request.LogInRequest;
import com.peluqueria.payload.request.SignUpRequest;
import com.peluqueria.payload.response.JwtResponse;
import com.peluqueria.payload.response.MessageResponse;
import com.peluqueria.repository.UsuarioRepository; // Repositorio para buscar/guardar User
import com.peluqueria.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController { // Clase AuthController

    @Autowired AuthenticationManager authenticationManager;
    @Autowired UsuarioRepository userRepository;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtUtils jwtUtils;

    // --- 1. ENDPOINT DE INICIO DE SESIÓN ---
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogInRequest loginRequest) {

        // 1. Autentica al usuario usando email y password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // 2. Guarda la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Genera el token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Obtiene los detalles del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. Extrae el rol del usuario para incluirlo en la respuesta
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 6. Retorna el JWT y los datos del usuario
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles.get(0))); // Retorna el rol principal
    }

    // --- 2. ENDPOINT DE REGISTRO DE USUARIOS ---
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {

        // 1. Validación: Verifica si el email ya está en uso
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El Email ya está en uso!"));
        }

        // 2. Crea la entidad User y codifica la contraseña
        Usuario user = new Usuario(
                signupRequest.getNombre(),
                signupRequest.getApellidos(), // AÑADIDO: Campo apellidos
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())
        );

        // 3. Asigna el rol (usa el rol proporcionado o 'CLIENTE' por defecto)
        String rol = signupRequest.getRol() == null ? "CLIENTE" : signupRequest.getRol();
        user.setRol(rol);

        // 4. Guarda el usuario en la base de datos
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente!"));
    }
}**/