package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
}
