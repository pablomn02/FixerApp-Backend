package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.EstadoContratacion;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface ContratacionService {

    Contratacion save(Contratacion contratacion);

    List<Contratacion> getAll();

    List<String> getHorasOcupadas(Long idProfesionalServicio, String fecha);

    void validarDisponibilidad(ProfesionalServicio profesionalServicio, LocalDateTime fechaHoraUTC, int duracion);

    void crearContratacion(ContratacionCreateRequest request);

    List<Contratacion> findByClienteIdAndEstadoIn(Long idCliente, List<EstadoContratacion> estados);

    List<Contratacion> findByProfesionalId(Long id);

    void actualizarEstado(Long id, EstadoContratacion estadoContratacion);

    List<LocalTime> getBloquesDisponibles(Long idProfesionalServicio, LocalDate localDate);

    List<Contratacion> findByClienteId(Long idCliente);
}
