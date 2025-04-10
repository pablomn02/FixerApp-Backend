package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.Servicio;
import org.example.fixerappbackend.repo.ServicioRepo;
import org.example.fixerappbackend.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepo servicioRepo;

    @Override
    public List<Servicio> findAll() {
        return servicioRepo.findAll();
    }

    @Override
    public List<Servicio> findServiciosByCategoriaId(Integer idCategoria) {
        return servicioRepo.findServiciosByCategoriaId(idCategoria);
    }
}
