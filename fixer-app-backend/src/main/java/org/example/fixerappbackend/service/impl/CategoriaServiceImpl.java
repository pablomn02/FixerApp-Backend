package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.Categoria;
import org.example.fixerappbackend.repo.CategoriaRepo;
import org.example.fixerappbackend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    private CategoriaRepo categoriaRepo;

    @Override
    public List<Categoria> findAll() {
        return categoriaRepo.findAll();
    }
}
