package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepo extends JpaRepository<Administrador, Long> {
}
