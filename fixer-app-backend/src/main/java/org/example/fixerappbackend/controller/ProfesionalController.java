package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.dto.ProfesionalServicioDTO;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.repo.ProfesionalRepo;
import org.example.fixerappbackend.repo.ProfesionalServicioRepo;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalService;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private ProfesionalServicioRepo profesionalServicioRepository;

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

    private double distanciaEnKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @GetMapping("/servicio-cercanos")
    public ResponseEntity<List<ProfesionalServicioDTO>> getProfesionalesByServicioCercanos(
            @RequestParam Long servicioId,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "100") double radioKm) {

        List<ProfesionalServicio> profesionalServicios = profesionalServicioService.findByServicioId(servicioId);

        List<ProfesionalServicioDTO> filtrados = profesionalServicios.stream()
                .filter(ps -> {
                    Profesional p = ps.getProfesional();
                    return distanciaEnKm(
                            lat,
                            lon,
                            p.getLatitude().doubleValue(),
                            p.getLongitude().doubleValue()
                    ) <= radioKm;
                })

                .map(ps -> new ProfesionalServicioDTO(ps.getProfesional(), ps))
                .toList();

        return ResponseEntity.ok(filtrados);
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getIdProfesionalServicio(@RequestParam Long usuarioId, @RequestParam Long servicioId) {
        Optional<ProfesionalServicio> ps = profesionalServicioRepository.findByProfesional_IdAndServicio_Id(usuarioId, servicioId);
        return ps.map(profServ -> ResponseEntity.ok(profServ.getId()))
                .orElse(ResponseEntity.notFound().build());
    }


}
