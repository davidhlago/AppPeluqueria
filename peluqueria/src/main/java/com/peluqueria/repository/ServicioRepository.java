package com.peluqueria.repository;

import com.peluqueria.entity.Servicio;
import com.peluqueria.entity.TipoServicio; // <-- IMPORTAR LA NUEVA ENTIDAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByNombreContainingIgnoreCase(String nombre);


    List<Servicio> findByTipoServicio(TipoServicio tipoServicio);

    @Query(
            value = "SELECT * FROM servicio s WHERE s.duracion_bloques > :minDuracion",
            nativeQuery = true)
    List<Servicio> buscarPorDuracionMinima(@Param("minDuracion") int minDuracion);
}