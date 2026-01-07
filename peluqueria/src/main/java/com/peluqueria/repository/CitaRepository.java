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

    List<Cita> findByCliente_Id(Long idCliente);
    List<Cita> findByGrupo_Id(Long idGrupo);

    // CAMBIO IMPORTANTE: Ahora filtramos por horarioSemanal.idHorarioSemana
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.horarioSemanal.idHorarioSemana = :idHorario AND c.fecha = :fecha AND c.estado <> 'CANCELADA' AND ((c.horaInicio < :horaFin AND c.horaFin > :horaInicio))")
    long countCitasConflictivas(@Param("idHorario") Long idHorario, @Param("fecha") LocalDate fecha, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);
}