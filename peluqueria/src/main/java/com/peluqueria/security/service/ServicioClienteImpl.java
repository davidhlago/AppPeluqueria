package com.peluqueria.security.service;

import com.peluqueria.entity.Cliente;
import com.peluqueria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicioClienteImpl implements ServicioCliente {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow();
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente detallesCliente) {
        Cliente cliente = obtenerClientePorId(id);
        cliente.setNombre(detallesCliente.getNombre());
        cliente.setApellidos(detallesCliente.getApellidos());
        cliente.setEmail(detallesCliente.getEmail());
        cliente.setTelefono(detallesCliente.getTelefono());
        cliente.setObservacion(detallesCliente.getObservacion());
        cliente.setAlergenos(detallesCliente.getAlergenos());
        cliente.setDireccion(detallesCliente.getDireccion());
        cliente.setGrupo(detallesCliente.getGrupo());
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public List<Cliente> buscarObservacionesOAlergenos(String texto) {
        return clienteRepository.buscarEnObservacionesOAlergenos(texto);
    }

    @Override
    public List<Cliente> obtenerClientesPorGrupo(Long idGrupo) {
        return clienteRepository.findByGrupoId(idGrupo);
    }
}