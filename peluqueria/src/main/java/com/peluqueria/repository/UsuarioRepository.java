package com.peluqueria.repository;

import com.peluqueria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Ejemplo de query personalizada: buscar por email
    Usuario findByEmail(String email);

    // Ejemplo: buscar por rol
    java.util.List<Usuario> findByRol(String rol);
}
