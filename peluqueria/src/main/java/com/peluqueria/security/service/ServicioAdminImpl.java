package com.peluqueria.security.service;

import com.peluqueria.entity.Admin;
import com.peluqueria.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioAdminImpl implements ServicioAdmin {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin guardarAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> obtenerTodosLosAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin obtenerAdminPorId(Long id) {
        return adminRepository.findById(id).orElseThrow();
    }

    @Override
    public void eliminarAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public List<Admin> buscarPorEspecialidad(String texto) {
        return adminRepository.buscarPorEspecialidad(texto);
    }
}