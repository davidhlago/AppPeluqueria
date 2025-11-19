package com.peluqueria.service;

import com.peluqueria.entity.Cliente;
import com.peluqueria.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioCliente {

    private final ClienteRepository clienteRepository;

    public ServicioCliente(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Cliente actualizarCliente(Long id, Cliente detallesCliente) {
        Cliente cliente = obtenerClientePorId(id);
        cliente.setTelefono(detallesCliente.getTelefono());
        cliente.setObservacion(detallesCliente.getObservacion());
        cliente.setAlergenos(detallesCliente.getAlergenos());
        cliente.setDireccion(detallesCliente.getDireccion());
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        clienteRepository.deleteById(id);
    }
}
