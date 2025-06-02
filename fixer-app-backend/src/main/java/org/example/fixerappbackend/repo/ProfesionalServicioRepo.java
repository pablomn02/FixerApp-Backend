package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesionalServicioRepo extends JpaRepository<ProfesionalServicio, Long> {
    List<ProfesionalServicio> findByServicio_Id(Long idServicio);

    Optional<ProfesionalServicio> findByProfesional_IdAndServicio_Id(Long profesionalId, Long servicioId);
}
