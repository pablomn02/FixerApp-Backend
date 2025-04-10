package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();

    Optional<Usuario> findById(Integer id);
}
