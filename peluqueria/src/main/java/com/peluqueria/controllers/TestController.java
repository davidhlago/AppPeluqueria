package com.peluqueria.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Contenido Público.";
    }


    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    public String userAccess() {
        return "Contenido de Cliente.";
    }


    @GetMapping("/grupo")
    @PreAuthorize("hasAuthority('GRUPO')")
    public String moderatorAccess() {
        return "Grupo Board.";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}