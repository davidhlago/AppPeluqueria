package com.peluqueria.security.service;

import com.peluqueria.entity.Admin;
import java.util.List;

public interface ServicioAdmin {
    Admin guardarAdmin(Admin admin);
    List<Admin> obtenerTodosLosAdmins();
    Admin obtenerAdminPorId(Long id);
    void eliminarAdmin(Long id);
    List<Admin> buscarPorEspecialidad(String texto);
}