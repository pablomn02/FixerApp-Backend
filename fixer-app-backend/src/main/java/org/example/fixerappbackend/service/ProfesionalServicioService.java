package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.ProfesionalServicio;

import java.util.List;
import java.util.Optional;

public interface ProfesionalServicioService {
    Optional<ProfesionalServicio> findById(Long idProfesionalServicio);

    List<ProfesionalServicio> findByServicioId(Long idServicio);
}
