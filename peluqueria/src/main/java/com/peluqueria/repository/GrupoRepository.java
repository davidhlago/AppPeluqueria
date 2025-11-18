package com.peluqueria.repository;

import com.peluqueria.project.modelo.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    Optional<Grupo> findByNombre(String nombre);
}