package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.service.ClienteService;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contrataciones")
public class ContratacionController {
    @Autowired
    private ContratacionService contratacionService;

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProfesionalServicioService profesionalServicioService;

    @GetMapping
    public ResponseEntity<List<ContratacionDTO>> getAll() {
        List<Contratacion> contrataciones = contratacionService.getAll();
        List<ContratacionDTO> dtos = contrataciones.stream()
                .map(ContratacionDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ContratacionDTO> crearContratacion(@RequestBody ContratacionCreateRequest request) {
        Cliente cliente = clienteService.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ProfesionalServicio profServ = profesionalServicioService.findById(request.getIdProfesionalServicio())
                .orElseThrow(() -> new RuntimeException("ProfesionalServicio no encontrado"));

        Contratacion contratacion = new Contratacion();
        contratacion.setCliente(cliente);
        contratacion.setProfesionalServicio(profServ);
        contratacion.setFechaHora(request.getFechaHora());
        contratacion.setDuracionEstimada(request.getDuracionEstimada());
        contratacion.setCostoTotal(request.getCostoTotal());
        contratacion.setEstado("pendiente");

        Contratacion creada = contratacionService.save(contratacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContratacionDTO.fromEntity(creada));
    }
}
