package com.peluqueria.repository;

import com.peluqueria.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Ejemplo: buscar por especialidad
    java.util.List<Admin> findByEspecialidad(String especialidad);
}
