package com.peluqueria.repository;

import com.peluqueria.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.servicio.idServicio = :idServicio " +
            "AND c.fecha = :fecha " +
            "AND c.estado != 'CANCELADA' " +
            "AND ( " +
            "  (c.horaInicio < :fin AND c.horaFin > :inicio) " +
            ")")
    long countCitasConflictivas(@Param("idServicio") Long idServicio,
                                @Param("fecha") LocalDate fecha,
                                @Param("inicio") LocalTime inicio,
                                @Param("fin") LocalTime fin);

    List<Cita> findByCliente_Id(Long id);

    List<Cita> findByGrupo_Id(Long id);
}