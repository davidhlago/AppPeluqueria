package com.peluqueria.repository;

import com.peluqueria.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    // Ejemplo: buscar por curso
    java.util.List<Grupo> findByCurso(String curso);

    // Ejemplo: buscar por turno
    java.util.List<Grupo> findByTurno(String turno);
}
