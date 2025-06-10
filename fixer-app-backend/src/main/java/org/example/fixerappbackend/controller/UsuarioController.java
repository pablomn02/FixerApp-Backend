package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ActualizarUsusarioDTO;
import org.example.fixerappbackend.model.Usuario;
import org.example.fixerappbackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @CrossOrigin("*")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody ActualizarUsusarioDTO dto) {
        Optional<Usuario> optUsuario = usuarioService.findById(id);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = optUsuario.get();

        usuario.setNombre(dto.getNombre());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setEmail(dto.getEmail());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            String hashed = new BCryptPasswordEncoder().encode(dto.getContrasena());
            usuario.setContrasena(hashed);
        }

        usuarioService.save(usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> optUsuario = usuarioService.findById(id);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}