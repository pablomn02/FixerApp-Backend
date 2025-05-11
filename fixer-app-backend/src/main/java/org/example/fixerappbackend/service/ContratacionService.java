package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.Contratacion;

import java.util.List;

public interface ContratacionService {

    Contratacion save(Contratacion contratacion);

    List<Contratacion> getAll();
}
