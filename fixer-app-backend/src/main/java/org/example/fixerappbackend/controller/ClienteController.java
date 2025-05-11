package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin("*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> getAll() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Cliente> getById(@PathVariable Long id) {
        return clienteService.findById(id);
    }

//    @PostMapping
//    public Cliente create(@RequestBody ClienteDTO dto) {
//        return clienteService.save(dto);
//    }
//
//    @PutMapping("/{id}")
//    public Cliente update(@PathVariable Long id, @RequestBody ClienteDTO dto) {
//        dto.setId(id);
//        return clienteService.update(dto);
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clienteService.delete(id);
    }
}
