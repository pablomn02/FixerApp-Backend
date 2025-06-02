package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.service.ClienteService;
import org.example.fixerappbackend.service.ContratacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ContratacionService contratacionService;

    @GetMapping
    public List<Cliente> getAll() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Cliente> getById(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }

    @GetMapping("/{id}/contrataciones")
    public List<ContratacionDTO> getContratacionesByCliente(@PathVariable Long id) {
        List<Contratacion> contrataciones = contratacionService.findByClienteId(id);
        System.out.println("Contrataciones para cliente " + id + ": " + contrataciones.size());
        return contrataciones.stream()
                .map(ContratacionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/{clienteId}/favoritos/{profesionalId}")
    public ResponseEntity<Set<Profesional>> addFavorito(
            @PathVariable Long clienteId,
            @PathVariable Long profesionalId) {
        return ResponseEntity.ok(
                clienteService.addFavorito(clienteId, profesionalId)
        );
    }

    @DeleteMapping("/{clienteId}/favoritos/{profesionalId}")
    public ResponseEntity<Set<Profesional>> removeFavorito(
            @PathVariable Long clienteId,
            @PathVariable Long profesionalId) {
        return ResponseEntity.ok(
                clienteService.removeFavorito(clienteId, profesionalId)
        );
    }

    @GetMapping("/{clienteId}/favoritos")
    public ResponseEntity<Set<Profesional>> getFavoritos(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(
                clienteService.getFavoritos(clienteId)
        );
    }

}
