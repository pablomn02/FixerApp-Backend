package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);

    Usuario save(Usuario usuario);
}
