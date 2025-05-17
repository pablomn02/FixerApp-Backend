package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ContratacionService {

    Contratacion save(Contratacion contratacion);

    List<Contratacion> getAll();

    List<String> getHorasOcupadas(Long idProfesionalServicio, String fecha);

    void validarDisponibilidad(ProfesionalServicio profesionalServicio, LocalDateTime fechaHoraUTC, int duracion);

    ResponseEntity<?> crearContratacion(ContratacionCreateRequest request);
}
