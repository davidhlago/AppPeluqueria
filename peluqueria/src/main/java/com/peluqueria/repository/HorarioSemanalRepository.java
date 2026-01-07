package com.peluqueria.repository;

import com.peluqueria.entity.HorarioSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioSemanalRepository extends JpaRepository<HorarioSemanal, Long> {

    // Método principal usado en el ServicioCitaImpl
    // Busca todos los horarios de un servicio para un día concreto (ej: "Lunes")
    // Devuelve LISTA porque puede haber turno de mañana y tarde el mismo día
    List<HorarioSemanal> findByServicio_IdServicioAndDiasSemana(Long idServicio, String diasSemana);

    // Mantenemos tus métodos útiles, pero devolviendo List para evitar errores
    List<HorarioSemanal> findByGrupo_Id(Long idGrupo);

    List<HorarioSemanal> findByServicio_IdServicio(Long idServicio);

    // Si quieres mantener tu consulta personalizada, actualízala así:
    // 1. Devuelve List (no Optional)
    // 2. Busca coincidencia exacta del día (mejor que LIKE si usamos los datos limpios del script)
    @Query("SELECT h FROM HorarioSemanal h WHERE h.grupo.id = :grupoId " +
            "AND h.servicio.idServicio = :servicioId " +
            "AND h.diasSemana = :dia")
    List<HorarioSemanal> buscarDisponibilidadPorGrupo(@Param("grupoId") Long grupoId,
                                                      @Param("servicioId") Long servicioId,
                                                      @Param("dia") String dia);
}