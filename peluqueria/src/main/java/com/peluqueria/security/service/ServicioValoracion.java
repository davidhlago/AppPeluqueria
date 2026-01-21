package com.peluqueria.security.service;

import com.peluqueria.entity.Valoracion;

import java.util.List;

public interface ServicioValoracion {
    Valoracion crearValoracion(Valoracion valoracion, Long idCita);
    Valoracion actualizarValoracion(Long idValoracion, Valoracion datos);
    void eliminarValoracion(Long idValoracion);
    List<Valoracion> obtenerPorCita(Long idCita);
    List<Valoracion> obtenerTodas();
}
