package com.peluqueria.repository;

import com.peluqueria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByTelefono(String telefono);

    java.util.List<Cliente> findByDireccionContaining(String direccion);

    java.util.List<Cliente> findByAlergenosContaining(String alergenos);
}
