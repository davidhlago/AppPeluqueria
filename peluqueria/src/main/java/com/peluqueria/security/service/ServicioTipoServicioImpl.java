package com.peluqueria.security.service;

import com.peluqueria.entity.TipoServicio;
import com.peluqueria.repository.TipoServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioTipoServicioImpl implements ServicioTipoServicio {

    @Autowired
    private TipoServicioRepository tipoServicioRepository;

    @Override
    public List<TipoServicio> obtenerTodos() {
        return tipoServicioRepository.findAll();
    }

    @Override
    public TipoServicio obtenerPorId(Long id) {
        Optional<TipoServicio> tipo = tipoServicioRepository.findById(id);
        return tipo.orElseThrow();
    }

    @Override
    public TipoServicio guardar(TipoServicio tipoServicio) {
        return tipoServicioRepository.save(tipoServicio);
    }

    @Override
    public void eliminar(Long id) {
        tipoServicioRepository.deleteById(id);
    }

    @Override
    public List<TipoServicio> buscarPorTexto(String texto) {
        return tipoServicioRepository.buscarPorLetras(texto);
    }
}