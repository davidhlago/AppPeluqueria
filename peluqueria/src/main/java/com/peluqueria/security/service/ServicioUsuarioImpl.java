package com.peluqueria.security.service;

import com.peluqueria.entity.Usuario;
import com.peluqueria.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioUsuarioImpl {

    private final UsuarioRepository usuarioRepository;

    public ServicioUsuarioImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Usuario actualizarUsuario(Long id, Usuario detalles) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setNombre(detalles.getNombre());
        usuario.setApellidos(detalles.getApellidos());
        usuario.setEmail(detalles.getEmail());
        usuario.setPassword(detalles.getPassword());
        usuario.setRol(detalles.getRol());
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        usuarioRepository.deleteById(id);
    }
}
