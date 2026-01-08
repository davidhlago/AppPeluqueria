package com.peluqueria.repository;

import com.peluqueria.entity.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioSemanalRepository extends JpaRepository<HorarioSemanal, Long> {

    // Trae todos los horarios con sus servicios y grupos en 1 sola consulta
    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio JOIN FETCH h.grupo")
    List<HorarioSemanal> findAllOptimized();

    // Trae horarios de un grupo específico optimizado
    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio JOIN FETCH h.grupo WHERE h.grupo.id = :idGrupo")
    List<HorarioSemanal> findByGrupo_IdOptimized(@Param("idGrupo") Long idGrupo);

    // Los métodos que ya tenías (puedes mantenerlos o borrarlos si usas los de arriba)
    List<HorarioSemanal> findByServicio_IdServicioAndDiasSemana(Long idServicio, String diasSemana);

    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio JOIN FETCH h.grupo WHERE h.servicio.idServicio = :idServicio")
    List<HorarioSemanal> findByServicio_IdServicioOptimized(@Param("idServicio") Long idServicio);
}