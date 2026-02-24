package com.peluqueria.repository;

import com.peluqueria.entity.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HorarioSemanalRepository extends JpaRepository<HorarioSemanal, Long> {

    // --- MÉTODOS EXISTENTES ---
    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio JOIN FETCH h.grupo")
    List<HorarioSemanal> findAllOptimized();

    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio JOIN FETCH h.grupo WHERE h.grupo.id = :idGrupo")
    List<HorarioSemanal> findByGrupo_IdOptimized(@Param("idGrupo") Long idGrupo);

    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio JOIN FETCH h.grupo WHERE h.servicio.idServicio = :idServicio")
    List<HorarioSemanal> findByServicio_IdServicioOptimized(@Param("idServicio") Long idServicio);

    // 👇👇👇 CORRECCIÓN BLINDADA AQUÍ 👇👇👇
    // Usamos @Query para asegurar que busca por el ID del servicio y el día exacto
    // Y usamos JOIN FETCH para traer los datos del servicio de una vez (evita error
    // Lazy)
    @Query("SELECT h FROM HorarioSemanal h JOIN FETCH h.servicio WHERE h.servicio.idServicio = :idServicio AND h.diasSemana = :dia")
    List<HorarioSemanal> buscarPorServicioYDia(@Param("idServicio") Long idServicio, @Param("dia") String dia);

    // HorarioSemanalRepository.java
    @Query("SELECT DISTINCT h.diasSemana FROM HorarioSemanal h WHERE h.servicio.idServicio = :idServicio")
    List<String> findDiasByServicioId(@Param("idServicio") Long idServicio);

    @Query("SELECT h FROM HorarioSemanal h WHERE h.grupo.id = :idGrupo AND h.diasSemana = :diaSemana")
    List<HorarioSemanal> findByGrupoAndDia(@Param("idGrupo") Long idGrupo, @Param("diaSemana") String diaSemana);
}