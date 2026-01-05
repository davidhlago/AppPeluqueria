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

    List<Cita> findByClienteId(Long id);
    List<Cita> findByGrupoId(Long id);
    List<Cita> findByAlumnoId(Long id);

    @Query("SELECT c FROM Cita c WHERE c.grupo.id = :grupoId " +
            "AND c.fecha = :fecha " +
            "AND c.estado != 'CANCELADA' " +
            "AND ((c.horaInicio < :horaFin) AND (c.horaFin > :horaInicio))")
    List<Cita> encontrarCitasSolapadas(@Param("grupoId") Long grupoId,
                                       @Param("fecha") LocalDate fecha,
                                       @Param("horaInicio") LocalTime horaInicio,
                                       @Param("horaFin") LocalTime horaFin);

    @Query("SELECT c FROM Cita c WHERE c.grupo.id = :grupoId " +
            "AND c.fecha = :fecha " +
            "AND c.idCita != :citaId " +
            "AND c.estado != 'CANCELADA' " +
            "AND ((c.horaInicio < :horaFin) AND (c.horaFin > :horaInicio))")
    List<Cita> encontrarCitasSolapadasEditar(@Param("grupoId") Long grupoId,
                                             @Param("fecha") LocalDate fecha,
                                             @Param("horaInicio") LocalTime horaInicio,
                                             @Param("horaFin") LocalTime horaFin,
                                             @Param("citaId") Long citaId);

    @Query("SELECT COUNT(c) FROM Cita c WHERE c.grupo.id = :grupoId " +
            "AND c.servicio.idServicio = :servicioId " +
            "AND c.fecha = :fecha " +
            "AND c.estado != 'CANCELADA'")
    long contarCitasPorDia(@Param("grupoId") Long grupoId,
                           @Param("servicioId") Long servicioId,
                           @Param("fecha") LocalDate fecha);
}