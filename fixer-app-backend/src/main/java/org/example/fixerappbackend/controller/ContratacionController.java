package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.EstadoContratacion;
import org.example.fixerappbackend.repo.UsuarioRepo;
import org.example.fixerappbackend.service.ClienteService;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contrataciones")
public class ContratacionController {

    @Autowired
    private ContratacionService contratacionService;

    @Autowired
    private ProfesionalServicioService profesionalServicioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @GetMapping
    public List<Contratacion> getAllContrataciones() {
        return contratacionService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> crearContratacion(@RequestBody ContratacionCreateRequest request) {
        return contratacionService.crearContratacion(request);
    }

    @GetMapping("/ocupadas/{idProfesionalServicio}/{fecha}")
    public List<String> getHorasOcupadas(
            @PathVariable Long idProfesionalServicio,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fecha) {
        return contratacionService.getHorasOcupadas(idProfesionalServicio, fecha);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<ContratacionDTO>> getContratacionesActivas(@PathVariable Long id) {
        List<EstadoContratacion> estados = List.of(EstadoContratacion.PENDIENTE, EstadoContratacion.ACEPTADA);
        List<Contratacion> contrataciones = contratacionService.findByClienteIdAndEstadoIn(id, estados);
        List<ContratacionDTO> resultado = contrataciones.stream()
                .map(ContratacionDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}/modificarEstadoContratacion")
    public ResponseEntity<?> modificarEstadoContratacion(
            @PathVariable Long id,
            @RequestParam EstadoContratacion nuevoEstado) {
        try {
            contratacionService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok().body("Estado actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar estado: " + e.getMessage());
        }
    }
}
