package com.peluqueria.repository;

import com.peluqueria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    List<Cliente> findByApellidosContainingIgnoreCase(String apellidos);

    List<Cliente> findByTelefonoContaining(String telefono);

    @Query("SELECT c FROM Cliente c WHERE c.observacion LIKE %:texto% OR c.alergenos LIKE %:texto%")
    List<Cliente> buscarEnObservacionesOAlergenos(@Param("texto") String texto);
}