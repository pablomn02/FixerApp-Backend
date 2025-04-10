package org.example.fixerappbackend.controller;


import org.example.fixerappbackend.model.Servicio;
import org.example.fixerappbackend.model.Usuario;
import org.example.fixerappbackend.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    @CrossOrigin("*")
    public List<Servicio> getAllServicios() {
        return servicioService.findAll();
    }

    @GetMapping("/{idCategoria}")
    @CrossOrigin("*")
    public List<Servicio> getServiciosByCategoriaId(@PathVariable Integer idCategoria) {
        return servicioService.findServiciosByCategoriaId(idCategoria);
    }
}
