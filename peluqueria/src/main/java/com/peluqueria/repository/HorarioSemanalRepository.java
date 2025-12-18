package com.peluqueria.repository;

import com.peluqueria.entity.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HorarioSemanalRepository extends JpaRepository<HorarioSemanal, Long> {
    List<HorarioSemanal> findByGrupoId(Long idGrupo);
    List<HorarioSemanal> findByServicioIdServicio(Long idServicio);
}