package com.peluqueria.security.service;

import com.peluqueria.entity.Admin;
import com.peluqueria.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioAdmin {

    private final AdminRepository adminRepository;

    public ServicioAdmin(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin guardarAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> obtenerTodosLosAdmins() {
        return adminRepository.findAll();
    }

    public Admin obtenerAdminPorId(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public void eliminarAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        adminRepository.deleteById(id);
    }
}
