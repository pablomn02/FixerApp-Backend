package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.HoraOcupada;
import org.example.fixerappbackend.repo.HoraOcupadaRepo;
import org.example.fixerappbackend.service.HoraOcupadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoraOcupadaServiceImpl implements HoraOcupadaService {
    @Autowired
    private HoraOcupadaRepo horaOcupadaRepo;
    @Override
    public List<HoraOcupada> findAll() {
        return horaOcupadaRepo.findAll();
    }
}
