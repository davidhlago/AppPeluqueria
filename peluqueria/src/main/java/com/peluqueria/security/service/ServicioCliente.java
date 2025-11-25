package com.peluqueria.security.service;

import com.peluqueria.entity.Cliente;
import java.util.List;

public interface ServicioCliente {
    Cliente guardarCliente(Cliente cliente);
    List<Cliente> obtenerTodosLosClientes();
    Cliente obtenerClientePorId(Long id);
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
    List<Cliente> buscarObservacionesOAlergenos(String texto);
}