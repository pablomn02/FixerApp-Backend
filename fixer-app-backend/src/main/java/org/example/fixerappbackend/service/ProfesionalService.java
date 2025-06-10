package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.dto.ProfesionalServicioDTO;

import java.util.List;
import java.util.Optional;

public interface ProfesionalService {
    List<ProfesionalServicioDTO> getAllProfesionales();
    List<ProfesionalServicioDTO> getProfesionalesByServicio(Long servicioId);
    List<ContratacionDTO> getContratacionesByProfesional(Long profesionalId);
    List<ProfesionalServicioDTO> getProfesionalesByServicioCercanos(Long servicioId, double lat, double lon, double radioKm);
    Optional<Long> getIdProfesionalServicio(Long usuarioId, Long servicioId);
}
