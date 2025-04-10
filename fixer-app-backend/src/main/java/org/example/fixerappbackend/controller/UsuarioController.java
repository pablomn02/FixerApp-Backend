package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.model.Usuario;
import org.example.fixerappbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @CrossOrigin("*")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    public Optional<Usuario> getUsuarioById(@PathVariable Integer id) {
        return usuarioService.findById(id);
    }


}
