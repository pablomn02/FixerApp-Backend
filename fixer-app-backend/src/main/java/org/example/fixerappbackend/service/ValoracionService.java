package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ValoracionDTO;
import org.example.fixerappbackend.model.Valoracion;

import java.util.List;

public interface ValoracionService {
    List<Valoracion> findAll();
    List<Valoracion> findByProfesionalId(Long id);
    Valoracion crearValoracion(Valoracion valoracion);
}
