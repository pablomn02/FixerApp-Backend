package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.model.HoraOcupada;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.HoraOcupadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/horas-ocupadas")
public class HoraOcupadaController {
    @Autowired
    private ContratacionService contratacionService;

    @GetMapping("/disponibilidad")
    public ResponseEntity<List<String>> getDisponibilidad(
            @RequestParam Long idProfesionalServicio,
            @RequestParam String fecha
    ) {
        LocalDate localDate = LocalDate.parse(fecha);
        List<LocalTime> disponibles = contratacionService.getBloquesDisponibles(idProfesionalServicio, localDate);
        List<String> result = disponibles.stream()
                .map(t -> t.toString().substring(0, 5))  // "HH:mm"
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<String>> getHorasOcupadas(
            @RequestParam Long idProfesionalServicio,
            @RequestParam String fecha
    ) {
        List<String> horas = contratacionService.getHorasOcupadas(idProfesionalServicio, fecha);
        return ResponseEntity.ok(horas);
    }
}
