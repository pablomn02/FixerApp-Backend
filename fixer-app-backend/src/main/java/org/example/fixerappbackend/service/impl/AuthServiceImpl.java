package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.RegisterRequest;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Profesional;
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
        if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getcontrasena())) {
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

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        LOGGER.info("Intentando registrar usuario con email: " + registerRequest.getEmail());

        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(registerRequest.getEmail()) != null) {
            LOGGER.warning("El email ya está registrado: " + registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email ya está registrado"));
        }

        // Validar que la contraseña no sea null
        if (registerRequest.getContrasena() == null || registerRequest.getContrasena().isEmpty()) {
            LOGGER.warning("La contraseña no puede ser nula o vacía");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La contraseña es requerida"));
        }

        // Crear usuario según el rol
        Usuario usuario;
        switch (registerRequest.getRol().toLowerCase()) {
            case "cliente":
                usuario = new Cliente();
                break;
            case "profesional":
                usuario = new Profesional();
                break;
            default:
                LOGGER.warning("Rol no válido: " + registerRequest.getRol());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Rol no válido. Use 'cliente' o 'profesional'"));
        }

        // Configurar los campos comunes
        usuario.setNombre(registerRequest.getNombre());
        usuario.setNombreUsuario(registerRequest.getusuario());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));
        usuario.setValoracion(0.0f);

        // Guardar en la base de datos
        usuarioRepository.save(usuario);
        LOGGER.info("Usuario registrado exitosamente: " + usuario.getEmail());

        // Generar token
        String token = jwtUtil.create(String.valueOf(usuario.getId()), usuario.getEmail());
        String rol = determinarRol(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "token", token,
                        "rol", rol,
                        "idUsuario", usuario.getId(),
                        "message", "Usuario registrado exitosamente"
                ));
    }

    private String determinarRol(Usuario usuario) {
        if (usuario instanceof Cliente) {
            return "cliente";
        } else if (usuario instanceof Profesional) {
            return "profesional";
        } else if (usuario instanceof org.example.fixerappbackend.model.Administrador) {
            return "administrador";
        }
        return "desconocido";
    }
}