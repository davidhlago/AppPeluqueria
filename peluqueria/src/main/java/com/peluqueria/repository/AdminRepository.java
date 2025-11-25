package com.peluqueria.repository;

import com.peluqueria.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    List<Admin> findByEspecialidadContainingIgnoreCase(String texto);

    List<Admin> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos);

    @Query("SELECT a FROM Admin a WHERE a.especialidad LIKE %:texto%")
    List<Admin> buscarPorEspecialidad(@Param("texto") String texto);
}