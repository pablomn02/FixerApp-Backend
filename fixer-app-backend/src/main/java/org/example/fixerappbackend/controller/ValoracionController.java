package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ValoracionDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.Valoracion;
import org.example.fixerappbackend.repo.ClienteRepo;
import org.example.fixerappbackend.repo.ContratacionRepo;
import org.example.fixerappbackend.repo.ProfesionalRepo;
import org.example.fixerappbackend.service.ValoracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/valoraciones")
public class ValoracionController {

    @Autowired
    private ValoracionService valoracionService;

    @Autowired
    private ClienteRepo clienteRepository;

    @Autowired
    private ProfesionalRepo profesionalRepository;

    @Autowired
    private ContratacionRepo contratacionRepository;

    @GetMapping
    public List<Valoracion> getAllValoraciones() {
        return valoracionService.findAll();
    }

    @GetMapping("/{id}")
    public List<ValoracionDTO> getValoracionesByIdProfesional(@PathVariable Long id) {
        List<Valoracion> valoraciones = valoracionService.findByProfesionalId(id);
        return valoraciones.stream().map(v -> {
            ValoracionDTO dto = new ValoracionDTO();
            dto.setId(v.getId().longValue());
            dto.setNombreCliente(v.getCliente().getNombre());
            dto.setComentario(v.getComentario());
            dto.setPuntuacion(v.getPuntuacion().longValue());
            dto.setFechaTimestamp(v.getFechaTimestamp());
            dto.setIdCliente(v.getCliente() != null ? v.getCliente().getId() : null);
            dto.setIdProfesional(v.getProfesional() != null ? v.getProfesional().getId() : null);
            dto.setIdContratacion(v.getContratacion() != null ? v.getContratacion().getId() : null);
            return dto;
        }).toList();
    }


    @PostMapping("/crear-valoracion")
    public ResponseEntity<?> crearValoracion(@RequestBody ValoracionDTO dto) {
        try {
            // Validar existencia
            Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Profesional profesional = profesionalRepository.findById(dto.getIdProfesional())
                    .orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

            Contratacion contratacion = contratacionRepository.findById(dto.getIdContratacion())
                    .orElseThrow(() -> new RuntimeException("Contratación no encontrada"));

            // Crear y asignar datos
            Valoracion valoracion = new Valoracion();
            valoracion.setCliente(cliente);
            valoracion.setProfesional(profesional);
            valoracion.setContratacion(contratacion);
            valoracion.setPuntuacion(dto.getPuntuacion());
            valoracion.setComentario(dto.getComentario());
            valoracion.setFechaTimestamp(Instant.now());

            Valoracion saved = valoracionService.crearValoracion(valoracion);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear la valoración: " + e.getMessage());
        }
    }
}
