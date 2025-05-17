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

import java.util.List;

@RestController
@RequestMapping("/horas-ocupadas")
public class HoraOcupadaController {
    @Autowired
    private ContratacionService contratacionService;
    @GetMapping
    public ResponseEntity<List<String>> getHorasOcupadas(
            @RequestParam Long idProfesionalServicio,
            @RequestParam String fecha
    ) {
        List<String> horas = contratacionService.getHorasOcupadas(idProfesionalServicio, fecha);
        return ResponseEntity.ok(horas);
    }
}
