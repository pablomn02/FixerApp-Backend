package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;

import java.time.LocalDateTime;
import java.util.List;

public interface ContratacionService {

    Contratacion save(Contratacion contratacion);

    List<Contratacion> getAll();

    void validarDisponibilidad(Profesional profesional, LocalDateTime fechaHora);

    List<String> getHorasOcupadas(Long idProfesionalServicio, String fecha);
}
