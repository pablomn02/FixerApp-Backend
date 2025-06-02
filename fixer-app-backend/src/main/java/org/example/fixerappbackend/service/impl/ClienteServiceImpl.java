package org.example.fixerappbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.repo.ClienteRepo;
import org.example.fixerappbackend.repo.ProfesionalRepo;
import org.example.fixerappbackend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepo clienteRepository;

    @Autowired
    private ProfesionalRepo profesionalRepo;

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return Optional.of(clienteRepository.findById(id).orElseThrow());
    }

    @Override
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    public Set<Profesional> addFavorito(Long clienteId, Long profesionalId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        Profesional profesional = profesionalRepo.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado"));

        cliente.addFavorito(profesional);
        clienteRepository.save(cliente);
        return cliente.getFavoritos();
    }

    public Set<Profesional> removeFavorito(Long clienteId, Long profesionalId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        Profesional profesional = profesionalRepo.findById(profesionalId)
                .orElseThrow(() -> new EntityNotFoundException("Profesional no encontrado"));

        cliente.removeFavorito(profesional);
        clienteRepository.save(cliente);
        return cliente.getFavoritos();
    }

    public Set<Profesional> getFavoritos(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        return cliente.getFavoritos();
    }
}
