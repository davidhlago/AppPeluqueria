package com.peluqueria.security.service;

import com.peluqueria.entity.Cliente;
import com.peluqueria.entity.Cita;
import com.peluqueria.entity.Valoracion;
import com.peluqueria.repository.ClienteRepository;
import com.peluqueria.repository.CitaRepository;
import com.peluqueria.repository.ValoracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ServicioClienteImpl implements ServicioCliente {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ValoracionRepository valoracionRepository;

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
        cliente.setImagenBase64(detallesCliente.getImagenBase64());
        cliente.setFichaTecnica(detallesCliente.getFichaTecnica());
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        List<Cita> citas = citaRepository.findByCliente_Id(id);
        for (Cita cita : citas) {
            List<Valoracion> valoraciones = valoracionRepository.findByCita(cita);
            valoracionRepository.deleteAll(valoraciones);
        }
        citaRepository.deleteAll(citas);
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