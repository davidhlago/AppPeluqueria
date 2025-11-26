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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)//la hora
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LogInRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        com.peluqueria.security.service.UserDetailsImpl userDetails =
                (com.peluqueria.security.service.UserDetailsImpl) authentication.getPrincipal();

        String rol = userDetails.getAuthorities().stream()//obtiene la lista de role
                .findFirst()//toma la primera autoridad
                .map(a -> a.getAuthority())//extrae el nombre del rol
                .orElse(null);//si no null

        String jwt = jwtUtils.generarToken(userDetails.getUsername(), rol);//crea el token usando nombre y rol

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getNombre(),
                userDetails.getApellidos(),
                userDetails.getUsername(),
                rol
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
