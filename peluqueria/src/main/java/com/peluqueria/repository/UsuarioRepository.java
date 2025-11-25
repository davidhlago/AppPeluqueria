package com.peluqueria.repository;

import com.peluqueria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    Usuario findByEmail(String email);

    List<Usuario> findByUsernameContainingIgnoreCase(String texto);

    List<Usuario> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellidos);

    @Query("SELECT u FROM Usuario u WHERE u.email LIKE %:texto%")
    List<Usuario> buscarPorEmailParcial(@Param("texto") String texto);
}