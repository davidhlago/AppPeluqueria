package com.peluqueria.security.service;

import com.peluqueria.entity.TipoServicio;
import java.util.List;

public interface ServicioTipoServicio {
    List<TipoServicio> obtenerTodos();
    TipoServicio obtenerPorId(Long id);
    TipoServicio guardar(TipoServicio tipoServicio);
    void eliminar(Long id);
    List<TipoServicio> buscarPorTexto(String texto);
}