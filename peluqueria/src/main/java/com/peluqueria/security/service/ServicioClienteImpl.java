package com.peluqueria.security.service;

import com.peluqueria.entity.Cliente;
import com.peluqueria.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServicioClienteImpl {

    private final ClienteRepository clienteRepository;

    public ServicioClienteImpl(ClienteRepository clienteRepository) {
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

    public Cliente actualizarCliente(Long id, Cliente detalles) {
        Cliente cliente = obtenerClientePorId(id);
        cliente.setTelefono(detalles.getTelefono());
        cliente.setObservacion(detalles.getObservacion());
        cliente.setAlergenos(detalles.getAlergenos());
        cliente.setDireccion(detalles.getDireccion());
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        clienteRepository.deleteById(id);
    }
}
