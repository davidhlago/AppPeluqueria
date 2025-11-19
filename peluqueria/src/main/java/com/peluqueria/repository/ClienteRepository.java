package com.peluqueria.repository;

import com.peluqueria.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Ejemplo: buscar por teléfono
    Cliente findByTelefono(String telefono);

    // Ejemplo: buscar por dirección
    java.util.List<Cliente> findByDireccionContaining(String direccion);

    // Ejemplo: buscar por alérgenos
    java.util.List<Cliente> findByAlergenosContaining(String alergenos);
}
