package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ClienteDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Profesional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ClienteService {
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    void delete(Long id);

    Set<Profesional> removeFavorito(Long clienteId, Long profesionalId);

    Set<Profesional> getFavoritos(Long clienteId);

    Set<Profesional> addFavorito(Long clienteId, Long profesionalId);
}
