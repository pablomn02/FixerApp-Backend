package org.example.fixerappbackend.service;

import org.example.fixerappbackend.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();

    Optional<Categoria> findById(Integer id);
}
