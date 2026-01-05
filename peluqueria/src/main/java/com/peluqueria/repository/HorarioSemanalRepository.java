package com.peluqueria.repository;

import com.peluqueria.entity.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioSemanalRepository extends JpaRepository<HorarioSemanal, Long> {

    List<HorarioSemanal> findByGrupoId(Long idGrupo);
    List<HorarioSemanal> findByServicioIdServicio(Long idServicio);

    // CONSULTA CLAVE: Busca si existe horario para ese grupo, servicio y día (ej: "Lunes")
    @Query("SELECT h FROM HorarioSemanal h WHERE h.grupo.id = :grupoId " +
            "AND h.servicio.idServicio = :servicioId " +
            "AND h.diasSemana LIKE %:dia%")
    Optional<HorarioSemanal> buscarDisponibilidad(@Param("grupoId") Long grupoId,
                                                  @Param("servicioId") Long servicioId,
                                                  @Param("dia") String dia);
}