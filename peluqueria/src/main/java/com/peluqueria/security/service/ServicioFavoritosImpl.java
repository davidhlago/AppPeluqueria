package com.peluqueria.security.service;

import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.Servicio;
import com.peluqueria.repository.ClienteRepository;
import com.peluqueria.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ServicioFavoritosImpl implements ServicioFavoritos {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    @Transactional // IMPORTANTE: Abre transacción para modificar la BD
    public Map<String, Object> toggleFavorito(Long idCliente, Long idServicio) {

        Map<String, Object> respuesta = new HashMap<>();

        // 1. Validar existencia
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + idServicio));

        // 2. Obtener el Set de favoritos
        Set<Servicio> misFavoritos = cliente.getServiciosFavoritos();

        // 3. Lógica: Si existe lo borra, si no existe lo añade
        if (misFavoritos.contains(servicio)) {
            misFavoritos.remove(servicio);
            respuesta.put("mensaje", "Eliminado de favoritos");
            respuesta.put("like", false); // Para que Flutter ponga el corazón gris
        } else {
            misFavoritos.add(servicio);
            respuesta.put("mensaje", "Agregado a favoritos");
            respuesta.put("like", true);  // Para que Flutter ponga el corazón rojo
        }

        // 4. Guardar Cliente (Hibernate actualiza la tabla intermedia 'favoritos_cliente_servicio' solo)
        clienteRepository.save(cliente);

        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esFavorito(Long idCliente, Long idServicio) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Buscamos en el Set si está el servicio (comparando por ID internamente)
        return cliente.getServiciosFavoritos().stream()
                .anyMatch(s -> s.getIdServicio().equals(idServicio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> obtenerFavoritosDelCliente(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Convertimos el Set a List para devolverlo (JSON prefiere Listas)
        return new ArrayList<>(cliente.getServiciosFavoritos());
    }
}