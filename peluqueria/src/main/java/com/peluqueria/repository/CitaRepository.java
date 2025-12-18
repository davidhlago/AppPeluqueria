package com.peluqueria.repository;

import com.peluqueria.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByGrupoId(Long grupoId);
    List<Cita> findByAlumnoId(Long alumnoId);
}