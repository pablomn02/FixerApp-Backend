package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.repo.ContratacionRepo;
import org.example.fixerappbackend.service.ContratacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratacionServiceImpl implements ContratacionService {
    @Autowired
    private ContratacionRepo contratacionRepo;
    @Override
    public List<Contratacion> getAll() {
        return contratacionRepo.findAll();
    }

    @Override
    public Contratacion save(Contratacion contratacion) {
        return contratacionRepo.save(contratacion);
    }
}
