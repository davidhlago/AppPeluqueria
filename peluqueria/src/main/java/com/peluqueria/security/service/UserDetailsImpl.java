package com.peluqueria.security.services;

import com.peluqueria.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String username;
    private String password;
    private String rol;

    public UserDetailsImpl(Long id, String nombre, String apellidos, String email,
                           String username, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public static UserDetailsImpl build(Usuario usuario) {
        return new UserDetailsImpl(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRol()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ✅ Devuelve la autoridad directamente desde el rol guardado en BD
        return Collections.singletonList(new SimpleGrantedAuthority(rol));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username; // usamos el campo username como identificador
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Getters adicionales ---
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}
