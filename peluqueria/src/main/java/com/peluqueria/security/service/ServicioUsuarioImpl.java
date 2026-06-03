package com.peluqueria.security.service;

import com.peluqueria.entity.Usuario;
import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.Admin;
import com.peluqueria.entity.Grupo;
import com.peluqueria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow();
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario detallesUsuario) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setNombre(detallesUsuario.getNombre());
        usuario.setApellidos(detallesUsuario.getApellidos());
        usuario.setEmail(detallesUsuario.getEmail());
        usuario.setUsername(detallesUsuario.getUsername());

        if (usuario instanceof Cliente && detallesUsuario instanceof Cliente) {
            Cliente c = (Cliente) usuario;
            Cliente dc = (Cliente) detallesUsuario;
            c.setTelefono(dc.getTelefono());
            c.setDireccion(dc.getDireccion());
            c.setObservacion(dc.getObservacion());
            c.setAlergenos(dc.getAlergenos());
            c.setGrupo(dc.getGrupo());
            c.setImagenBase64(dc.getImagenBase64());
            c.setFichaTecnica(dc.getFichaTecnica());
        } else if (usuario instanceof Admin && detallesUsuario instanceof Admin) {
            Admin a = (Admin) usuario;
            Admin da = (Admin) detallesUsuario;
            a.setEspecialidad(da.getEspecialidad());
        } else if (usuario instanceof Grupo && detallesUsuario instanceof Grupo) {
            Grupo g = (Grupo) usuario;
            Grupo dg = (Grupo) detallesUsuario;
            g.setCurso(dg.getCurso());
            g.setTurno(dg.getTurno());
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> buscarPorEmail(String texto) {
        return usuarioRepository.buscarPorEmailParcial(texto);
    }
}