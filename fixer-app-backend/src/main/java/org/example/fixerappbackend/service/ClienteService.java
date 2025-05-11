package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ClienteDTO;
import org.example.fixerappbackend.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    void delete(Long id);
}
