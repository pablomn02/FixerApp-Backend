package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.dto.ProfesionalServicioDTO;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalService;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalController {
    @Autowired
    private ProfesionalService profesionalService;

    @Autowired
    private ProfesionalServicioService profesionalServicioService;

    @Autowired
    private ContratacionService contratacionService;

    @GetMapping()
    public List<ProfesionalServicioDTO> getAllProfesionales() {
        List<Profesional> profesionales = profesionalService.findAll();
        List<ProfesionalServicioDTO> result = new ArrayList<>();
        for (Profesional profesional : profesionales) {
            for (ProfesionalServicio ps : profesional.getProfesionalServicios()) {
                result.add(new ProfesionalServicioDTO(profesional, ps));
            }
        }
        return result;
    }

    @GetMapping("/servicio/{id}")
    public List<ProfesionalServicioDTO> getProfesionalesByServicio(@PathVariable Long id) {
        List<ProfesionalServicio> profesionalServicios = profesionalServicioService.findByServicioId(id);

        return profesionalServicios.stream()
                .map(ps -> new ProfesionalServicioDTO(ps.getProfesional(), ps))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/contrataciones")
    public List<ContratacionDTO> getContratacionesByProfesional(@PathVariable Long id) {
        return contratacionService.findByProfesionalId(id).stream()
                .map(ContratacionDTO::fromEntity)
                .collect(Collectors.toList());
    }



}
