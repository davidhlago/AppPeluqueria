package com.peluqueria.security.service;

import com.peluqueria.entity.Servicio;
import java.util.List;
import java.util.Map;

public interface ServicioFavoritos {

    // Pone like si no tiene, quita like si ya tiene
    Map<String, Object> toggleFavorito(Long idCliente, Long idServicio);

    // Devuelve true si el cliente ya le dio like a ese servicio
    boolean esFavorito(Long idCliente, Long idServicio);

    // Devuelve la lista completa de servicios favoritos
    List<Servicio> obtenerFavoritosDelCliente(Long idCliente);
}