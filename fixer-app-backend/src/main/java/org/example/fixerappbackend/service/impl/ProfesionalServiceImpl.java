package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.repo.ProfesionalRepo;
import org.example.fixerappbackend.service.ProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesionalServiceImpl implements ProfesionalService {

    @Autowired
    private ProfesionalRepo profesionalRepo;
    @Override
    public List<Profesional> findAll() {
        return profesionalRepo.findAll();
    }
}
