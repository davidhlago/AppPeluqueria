package com.peluqueria.repository;

import com.peluqueria.entity.Valoracion;
import com.peluqueria.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {

    List<Valoracion> findByCita(Cita cita);

    // Media de puntuación de un servicio concreto (por idServicio)
    @Query("SELECT AVG(v.puntuacion) " +
            "FROM Valoracion v " +
            "WHERE v.cita.horarioSemanal.servicio.idServicio = :idServicio")
    Double mediaPuntuacionPorServicio(Long idServicio);

    // Todas las valoraciones de un servicio concreto
    @Query("SELECT v " +
            "FROM Valoracion v " +
            "WHERE v.cita.horarioSemanal.servicio.idServicio = :idServicio")
    List<Valoracion> findByServicio(Long idServicio);
}
