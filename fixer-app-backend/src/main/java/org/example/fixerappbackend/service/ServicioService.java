package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.Servicio;

import java.util.List;

public interface ServicioService {
    List<Servicio> findAll();

    List<Servicio> findServiciosByCategoriaId(Integer idCategoria);

    List<Profesional> obtenerProfesionalesPorServicio(Long servicioId);
}
