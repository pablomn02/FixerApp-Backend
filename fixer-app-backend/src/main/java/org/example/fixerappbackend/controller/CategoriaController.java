package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.model.Categoria;
import org.example.fixerappbackend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping()
    @CrossOrigin("*")
    public List<Categoria> listarCategorias() {
        return categoriaService.findAll();
    }

    @GetMapping("{id}")
    @CrossOrigin("*")
    public Optional<Categoria> getCategoriaById(@PathVariable Integer id) {
        return categoriaService.findById(id);
    }
}
