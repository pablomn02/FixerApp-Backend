package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesionalServicioRepo extends JpaRepository<ProfesionalServicio, Long> {
}
