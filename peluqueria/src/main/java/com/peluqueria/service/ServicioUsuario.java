package com.peluqueria.service;

import com.peluqueria.entity.Usuario;
import java.util.List;

public interface ServicioUsuario {
    Usuario guardarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Long id, Usuario detallesUsuario);
    void eliminarUsuario(Long id);
    Usuario obtenerUsuarioPorId(Long id);
    List<Usuario> obtenerTodosLosUsuarios();
    Usuario buscarUsuarioPorEmail(String email);
}