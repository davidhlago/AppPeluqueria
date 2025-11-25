package com.peluqueria.security.service;

import com.peluqueria.entity.Usuario;
import java.util.List;

public interface ServicioUsuario {
    List<Usuario> obtenerTodosLosUsuarios();
    Usuario obtenerUsuarioPorId(Long id);
    Usuario actualizarUsuario(Long id, Usuario usuario);
    void eliminarUsuario(Long id);
    List<Usuario> buscarPorEmail(String texto);
}