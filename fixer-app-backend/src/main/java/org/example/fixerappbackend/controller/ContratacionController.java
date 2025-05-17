package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.model.Usuario;
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
        try {
            ProfesionalServicio profesionalServicio = profesionalServicioService.findById(request.getIdProfesionalServicio())
                    .orElseThrow(() -> new RuntimeException("ProfesionalServicio no encontrado."));

            Usuario usuario = usuarioRepo.findById(request.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

            if (!(usuario instanceof Cliente)) {
                throw new RuntimeException("El usuario no es un cliente válido.");
            }

            Cliente cliente = (Cliente) usuario;

            Contratacion nueva = new Contratacion();
            nueva.setCliente(cliente); // ✅ Correcto
            nueva.setProfesionalServicio(profesionalServicio);
            nueva.setFechaHora(request.getFechaHora());
            nueva.setDuracionEstimada(request.getDuracionEstimada());
            nueva.setCostoTotal(request.getCostoTotal());
            nueva.setEstado("pendiente");

            contratacionService.validarDisponibilidad(profesionalServicio.getProfesional(), request.getFechaHora());

            Contratacion guardada = contratacionService.save(nueva);
            return ResponseEntity.ok(guardada);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    java.util.Map.of(
                            "error", "Internal Server Error",
                            "message", e.getMessage()
                    )
            );
        }
    }




    @GetMapping("/ocupadas/{idProfesionalServicio}/{fecha}")
    public List<String> getHorasOcupadas(
            @PathVariable Long idProfesionalServicio,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String fecha) {
        return contratacionService.getHorasOcupadas(idProfesionalServicio, fecha);
    }
}
