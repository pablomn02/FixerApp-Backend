package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Contratacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratacionRepo extends JpaRepository<Contratacion, Long> {
}
