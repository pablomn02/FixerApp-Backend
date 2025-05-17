package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);

    Usuario findByNombreUsuario(String nombre);

    boolean existsByNombreUsuario(String nombreUsuario);

    boolean existsByEmail(String email);
}