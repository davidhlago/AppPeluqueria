package com.peluqueria.security.service;

import com.peluqueria.entity.Usuario;
import com.peluqueria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario detallesUsuario) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setNombre(detallesUsuario.getNombre());
        usuario.setApellidos(detallesUsuario.getApellidos());
        usuario.setEmail(detallesUsuario.getEmail());
        usuario.setUsername(detallesUsuario.getUsername());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> buscarPorEmail(String texto) {
        return usuarioRepository.buscarPorEmailParcial(texto);
    }
}