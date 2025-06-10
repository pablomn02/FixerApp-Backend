package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.dto.ProfesionalServicioDTO;
import org.example.fixerappbackend.service.ProfesionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalController {

    @Autowired
    private ProfesionalService profesionalService;

    @GetMapping
    public List<ProfesionalServicioDTO> getAllProfesionales() {
        return profesionalService.getAllProfesionales();
    }

    @GetMapping("/servicio/{id}")
    public List<ProfesionalServicioDTO> getProfesionalesByServicio(@PathVariable Long id) {
        return profesionalService.getProfesionalesByServicio(id);
    }

    @GetMapping("/{id}/contrataciones")
    public List<ContratacionDTO> getContratacionesByProfesional(@PathVariable Long id) {
        return profesionalService.getContratacionesByProfesional(id);
    }

    @GetMapping("/servicio-cercanos")
    public ResponseEntity<List<ProfesionalServicioDTO>> getProfesionalesByServicioCercanos(
            @RequestParam Long servicioId,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "100") double radioKm) {

        List<ProfesionalServicioDTO> filtrados =
                profesionalService.getProfesionalesByServicioCercanos(servicioId, lat, lon, radioKm);
        return ResponseEntity.ok(filtrados);
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getIdProfesionalServicio(
            @RequestParam Long usuarioId,
            @RequestParam Long servicioId) {

        Optional<Long> optId = profesionalService.getIdProfesionalServicio(usuarioId, servicioId);
        return optId
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
