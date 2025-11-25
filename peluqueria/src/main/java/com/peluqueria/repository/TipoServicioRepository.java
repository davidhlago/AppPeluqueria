package com.peluqueria.repository;

import com.peluqueria.entity.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {

    List<TipoServicio> findByNombreContaining(String letras);

    List<TipoServicio> findByNombreContainingIgnoreCase(String letras);

    @Query("SELECT t FROM TipoServicio t WHERE t.nombre LIKE %:letras%")
    List<TipoServicio> buscarPorLetras(@Param("letras") String letras);

    @Query(value = "SELECT * FROM tipo_servicio WHERE nombre LIKE %:letras%", nativeQuery = true)
    List<TipoServicio> buscarPorLetrasNativo(@Param("letras") String letras);
}