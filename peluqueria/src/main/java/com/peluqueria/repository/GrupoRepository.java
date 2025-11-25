package com.peluqueria.repository;

import com.peluqueria.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    List<Grupo> findByCursoContainingIgnoreCase(String curso);

    List<Grupo> findByTurnoContainingIgnoreCase(String turno);

    @Query("SELECT g FROM Grupo g WHERE g.curso LIKE %:texto%")
    List<Grupo> buscarPorCurso(@Param("texto") String texto);
}