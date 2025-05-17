package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.repo.ProfesionalServicioRepo;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesionalServicioServiceImpl implements ProfesionalServicioService {

    @Autowired
    private ProfesionalServicioRepo profesionalServicioRepo;

    @Override
    public Optional<ProfesionalServicio> findById(Long idProfesionalServicio) {
        return profesionalServicioRepo.findById(idProfesionalServicio);
    }

    @Override
    public List<ProfesionalServicio> findByServicioId(Long idServicio) {
        return profesionalServicioRepo.findByServicio_Id(idServicio);
    }
}
