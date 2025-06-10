package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.dto.ProfesionalServicioDTO;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.repo.ProfesionalRepo;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfesionalServiceImpl implements ProfesionalService {

    @Autowired
    private ProfesionalRepo profesionalRepo;

    @Autowired
    private ContratacionService contratacionService;

    @Override
    public List<ProfesionalServicioDTO> getAllProfesionales() {
        List<Profesional> profesionales = profesionalRepo.findAll();
        List<ProfesionalServicioDTO> result = new ArrayList<>();

        for (Profesional profesional : profesionales) {
            Long profesionalId = profesional.getId();
            List<Contratacion> contrataciones = contratacionService.findByProfesionalId(profesionalId);
            Set<org.example.fixerappbackend.model.Valoracion> todasLasValoraciones = new HashSet<>();
            for (Contratacion c : contrataciones) {
                if (c.getValoraciones() != null) {
                    todasLasValoraciones.addAll(c.getValoraciones());
                }
            }
            // Todas las relaciones ProfesionalServicio de este profesional
            List<ProfesionalServicio> psList =
                    profesionalRepo.findProfesionalServiciosByProfesionalId(profesionalId);

            for (ProfesionalServicio ps : psList) {
                ProfesionalServicioDTO dto = new ProfesionalServicioDTO(profesional, ps);
                Double media = dto.calcularValoracionMedia(todasLasValoraciones);
                dto.setValoracionMedia(media);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public List<ProfesionalServicioDTO> getProfesionalesByServicio(Long servicioId) {
        List<Profesional> profesionales = profesionalRepo.findProfesionalesByServicioId(servicioId);

        return profesionales.stream().map(profesional -> {
                    Long profesionalId = profesional.getId();
                    Optional<ProfesionalServicio> optPs = profesionalRepo.findByProfesional_IdAndServicio_Id(profesionalId, servicioId);
                    if (optPs.isEmpty()) {
                        return null;
                    }
                    ProfesionalServicio ps = optPs.get();

                    List<Contratacion> contrataciones =
                            contratacionService.findByProfesionalId(profesionalId);
                    Set<org.example.fixerappbackend.model.Valoracion> todasLasValoraciones = new HashSet<>();
                    for (Contratacion c : contrataciones) {
                        if (c.getValoraciones() != null) {
                            todasLasValoraciones.addAll(c.getValoraciones());
                        }
                    }
                    ProfesionalServicioDTO dto = new ProfesionalServicioDTO(profesional, ps);
                    Double media = dto.calcularValoracionMedia(todasLasValoraciones);
                    dto.setValoracionMedia(media);
                    return dto;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContratacionDTO> getContratacionesByProfesional(Long profesionalId) {
        return contratacionService.findByProfesionalId(profesionalId).stream()
                .map(ContratacionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfesionalServicioDTO> getProfesionalesByServicioCercanos(
            Long servicioId, double lat, double lon, double radioKm) {

        List<Profesional> profesionales =
                profesionalRepo.findProfesionalesByServicioId(servicioId);

        return profesionales.stream().map(profesional -> {
                    double plat = profesional.getLatitude().doubleValue();
                    double plon = profesional.getLongitude().doubleValue();
                    double dLat = Math.toRadians(plat - lat);
                    double dLon = Math.toRadians(plon - lon);
                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                            + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(plat))
                            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double distancia = 6371 * c;
                    if (distancia > radioKm) {
                        return null;
                    }
                    Long profesionalId = profesional.getId();
                    Optional<ProfesionalServicio> optPs =
                            profesionalRepo.findByProfesional_IdAndServicio_Id(profesionalId, servicioId);
                    if (optPs.isEmpty()) {
                        return null;
                    }
                    ProfesionalServicio ps = optPs.get();

                    List<Contratacion> contrataciones = contratacionService.findByProfesionalId(profesionalId);
                    Set<org.example.fixerappbackend.model.Valoracion> todasLasValoraciones = new HashSet<>();
                    for (Contratacion contratacion : contrataciones) {
                        if (contratacion.getValoraciones() != null) {
                            todasLasValoraciones.addAll(contratacion.getValoraciones());
                        }
                    }
                    ProfesionalServicioDTO dto = new ProfesionalServicioDTO(profesional, ps);
                    Double media = dto.calcularValoracionMedia(todasLasValoraciones);
                    dto.setValoracionMedia(media);
                    return dto;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Long> getIdProfesionalServicio(Long usuarioId, Long servicioId) {
        return profesionalRepo
                .findByProfesional_IdAndServicio_Id(usuarioId, servicioId)
                .map(ProfesionalServicio::getId);
    }
}
