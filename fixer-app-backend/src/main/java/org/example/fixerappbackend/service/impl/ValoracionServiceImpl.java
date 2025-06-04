package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.ValoracionDTO;
import org.example.fixerappbackend.model.Valoracion;
import org.example.fixerappbackend.repo.ValoracionRepo;
import org.example.fixerappbackend.service.ValoracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoracionServiceImpl implements ValoracionService {

    @Autowired
    private ValoracionRepo valoracionRepo;

    @Override
    public List<Valoracion> findAll() {
        return valoracionRepo.findAll();
    }

    @Override
    public List<Valoracion> findByProfesionalId(Long id) {
        return valoracionRepo.findByProfesionalId(id);
    }

    @Override
    public Valoracion crearValoracion(Valoracion valoracion) {
        return valoracionRepo.save(valoracion);
    }
}
