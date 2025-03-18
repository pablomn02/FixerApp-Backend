package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.model.Usuario;
import org.example.fixerappbackend.repo.UsuarioRepo;
import org.example.fixerappbackend.service.AuthService;
import org.example.fixerappbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());

    @Autowired
    private UsuarioRepo usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LOGGER.info("Intentando login con email: " + loginRequest.getEmail());

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (usuario == null) {
            LOGGER.warning("Usuario no encontrado: " + loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        // Verificar contraseña
        LOGGER.info("Verificando contraseña para " + loginRequest.getEmail());
        if (!passwordEncoder.matches(loginRequest.getContraseña(), usuario.getContraseña())) {
            LOGGER.warning("Contraseña incorrecta para " + loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        // Generar token JWT
        LOGGER.info("Generando token para " + loginRequest.getEmail());
        String token = jwtUtil.create(String.valueOf(usuario.getId()), usuario.getEmail());

        // Determinar el rol del usuario
        String rol = determinarRol(usuario);

        // Respuesta con token, rol e ID
        return ResponseEntity.ok(Map.of(
                "token", token,
                "rol", rol,
                "idUsuario", usuario.getId()
        ));
    }

    private String determinarRol(Usuario usuario) {
        if (usuario instanceof org.example.fixerappbackend.model.Cliente) {
            return "cliente";
        } else if (usuario instanceof org.example.fixerappbackend.model.Profesional) {
            return "profesional";
        } else if (usuario instanceof org.example.fixerappbackend.model.Administrador) {
            return "administrador";
        }
        return "desconocido";
    }
}