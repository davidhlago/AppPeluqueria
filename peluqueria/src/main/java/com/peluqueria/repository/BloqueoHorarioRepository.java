package com.peluqueria.repository;

import com.peluqueria.entity.BloqueoHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BloqueoHorarioRepository extends JpaRepository<BloqueoHorario, Long> {

        List<BloqueoHorario> findByFecha(LocalDate fecha);

        List<BloqueoHorario> findByGrupoId(Long idGrupo);

        List<BloqueoHorario> findByServicioIdServicio(Long idServicio);

        List<BloqueoHorario> findByFechaAndGrupoId(LocalDate fecha, Long idGrupo);

        List<BloqueoHorario> findByFechaAndServicioIdServicio(LocalDate fecha, Long idServicio);

        // Consulta Maestra para detectar conflictos
        @Query("SELECT b FROM BloqueoHorario b WHERE " +
                        "b.fecha = :fecha " +
                        "AND (b.grupo IS NULL OR b.grupo.id = :idGrupo) " +
                        "AND (b.servicio IS NULL OR b.servicio.idServicio = :idServicio) " +
                        "AND (b.todoElDia = true OR " +
                        "    (:horaFinCita > b.horaInicio AND :horaInicioCita < b.horaFin))")
        List<BloqueoHorario> findConflictos(@Param("fecha") LocalDate fecha,
                        @Param("idGrupo") Long idGrupo,
                        @Param("idServicio") Long idServicio,
                        @Param("horaInicioCita") LocalTime horaInicioCita,
                        @Param("horaFinCita") LocalTime horaFinCita);

        // Buscar bloqueos activos entre dos fechas
        List<BloqueoHorario> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
