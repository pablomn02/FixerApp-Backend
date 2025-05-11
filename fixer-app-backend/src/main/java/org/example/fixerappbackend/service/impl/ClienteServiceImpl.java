package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.repo.ClienteRepo;
import org.example.fixerappbackend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepo clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return Optional.of(clienteRepository.findById(id).orElseThrow());
    }

//    @Override
//    public Cliente save(ClienteDTO dto) {
//        Cliente cliente = new Cliente();
//        cliente.setId(dto.getId());
//        cliente.setPreferencias(dto.getPreferencias());
//        return clienteRepository.save(cliente);
//    }
//
//    @Override
//    public Cliente update(ClienteDTO dto) {
//        Cliente cliente = findById(dto.getId());
//        cliente.setPreferencias(dto.getPreferencias());
//        return clienteRepository.save(cliente);
//    }

    @Override
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
