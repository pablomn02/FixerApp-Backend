package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.ClienteRegisterRequest;
import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.ProfesionalRegisterRequest;
import org.example.fixerappbackend.model.*;
import org.example.fixerappbackend.repo.PasswordResetTokenRepo;
import org.example.fixerappbackend.repo.ProfesionalServicioRepo;
import org.example.fixerappbackend.repo.ServicioRepo;
import org.example.fixerappbackend.repo.UsuarioRepo;
import org.example.fixerappbackend.service.AuthService;
import org.example.fixerappbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthServiceImpl.class.getName());

    @Autowired
    private UsuarioRepo usuarioRepository;

    @Autowired
    private PasswordResetTokenRepo tokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ServicioRepo servicioRepository;

    @Autowired
    private ProfesionalServicioRepo profesionalServicioRepo;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LOGGER.info("Intentando login con email: " + loginRequest.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (usuario == null) {
            LOGGER.warning("Usuario no encontrado: " + loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        LOGGER.info("Verificando contraseña para " + loginRequest.getEmail());
        if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getContrasena())) {
            LOGGER.warning("Contraseña incorrecta para " + loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        LOGGER.info("Generando token para " + loginRequest.getEmail());
        String token = jwtUtil.create(String.valueOf(usuario.getId()), usuario.getEmail());

        String rol = determinarRol(usuario);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "rol", rol,
                "idUsuario", usuario.getId()
        ));
    }

    public ResponseEntity<?> registerCliente(ClienteRegisterRequest registerRequest) {
        LOGGER.info("Intentando registrar cliente con email: " + registerRequest.getEmail());

        if (isEmailOrUsernameTaken(registerRequest.getEmail(), registerRequest.getUsuario())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email o nombre de usuario ya está registrado"));
        }

        if (isInvalidPassword(registerRequest.getContrasena())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La contraseña es requerida"));
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(registerRequest.getNombre());
        cliente.setNombreUsuario(registerRequest.getUsuario());
        cliente.setEmail(registerRequest.getEmail());
        cliente.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));
        cliente.setValoracion(0.0f);
        cliente.setPreferencias(registerRequest.getPreferencias());

        cliente.setTipoUsuario(TipoUsuario.valueOf("cliente"));

        usuarioRepository.save(cliente);
        LOGGER.info("Cliente registrado exitosamente: " + cliente.getEmail());

        String token = jwtUtil.create(String.valueOf(cliente.getId()), cliente.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "token", token,
                        "rol", "cliente",
                        "idUsuario", cliente.getId(),
                        "message", "Cliente registrado exitosamente"
                ));
    }


    public ResponseEntity<?> registerProfesional(ProfesionalRegisterRequest registerRequest) {
        LOGGER.info("Intentando registrar profesional con email: " + registerRequest.getEmail());

        if (isEmailOrUsernameTaken(registerRequest.getEmail(), registerRequest.getUsuario())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email o nombre de usuario ya está registrado"));
        }

        if (isInvalidPassword(registerRequest.getContrasena())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La contraseña es requerida"));
        }

        if (registerRequest.getEspecialidad() == null || registerRequest.getEspecialidad().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La especialidad es requerida"));
        }

        if (registerRequest.getPrecioHora() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El precio por hora es requerido"));
        }

        if (registerRequest.getUbicacion() == null ||
                registerRequest.getUbicacion().get("latitud") == null ||
                registerRequest.getUbicacion().get("longitud") == null) {
            LOGGER.warning("El campo 'ubicacion' es obligatorio para profesionales");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El campo 'ubicacion' es obligatorio y debe contener 'latitud' y 'longitud'"));
        }

        Profesional profesional = new Profesional();
        profesional.setNombre(registerRequest.getNombre());
        profesional.setNombreUsuario(registerRequest.getUsuario());
        profesional.setEmail(registerRequest.getEmail());
        profesional.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));
        profesional.setValoracion(0.0f);
        profesional.setEspecialidad(registerRequest.getEspecialidad());
        profesional.setPrecioHora(registerRequest.getPrecioHora());
        profesional.setHorarioDisponible(registerRequest.getHorarioDisponible());
        profesional.setExperiencia(registerRequest.getExperiencia());
        profesional.setCertificaciones(registerRequest.getCertificaciones());
        profesional.setCalificacionPromedio(0.0f);
        profesional.setTotalContrataciones(0);
        profesional.setLatitude(registerRequest.getLatitud());
        profesional.setLongitude(registerRequest.getLongitud());

        Servicio servicio = servicioRepository.findById(registerRequest.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        profesional.setServicio(servicio);

        profesional.setTipoUsuario(TipoUsuario.profesional);
        usuarioRepository.save(profesional);

        ProfesionalServicio relacion = new ProfesionalServicio();
        relacion.setProfesional(profesional);
        relacion.setServicio(servicio);
        relacion.setDescripcionServicio(registerRequest.getEspecialidad());
        relacion.setPrecio(registerRequest.getPrecioHora());

        profesionalServicioRepo.save(relacion);


        LOGGER.info("Profesional registrado exitosamente: " + profesional.getEmail());

        String token = jwtUtil.create(String.valueOf(profesional.getId()), profesional.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "token", token,
                        "rol", "profesional",
                        "idUsuario", profesional.getId(),
                        "message", "Profesional registrado exitosamente"
                ));

    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new Exception("El correo electrónico no está registrado.");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);
        PasswordResetToken resetToken = new PasswordResetToken(token, usuario, expiryDate);

        tokenRepository.deleteByUsuarioId(usuario.getId());
        tokenRepository.save(resetToken);

        sendPasswordResetEmail(usuario.getEmail(), token);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Token inválido o no encontrado."));

        if (resetToken.isExpired()) {
            throw new Exception("El token ha expirado.");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);
    }

    private boolean isEmailOrUsernameTaken(String email, String username) {
        return usuarioRepository.findByEmail(email) != null ||
                usuarioRepository.findByNombreUsuario(username) != null;
    }

    private boolean isInvalidPassword(String password) {
        return password == null || password.isEmpty();
    }

    private void sendPasswordResetEmail(String email, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        helper.setTo(email);
        helper.setSubject("Recuperación de Contraseña - Fixer");
        helper.setText(
                "<div style=\"font-family: 'Roboto', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f5f8; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">" +
                        "<h1 style=\"color: #3880ff; text-align: center; font-size: 24px; margin-bottom: 20px;\">Recuperación de Contraseña</h1>" +
                        "<p>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>" +
                        "<a href=\"" + resetUrl + "\">Restablecer Contraseña</a>" +
                        "</div>", true);

        mailSender.send(message);
    }

    private String determinarRol(Usuario usuario) {
        if (usuario instanceof Cliente) {
            return "cliente";
        } else if (usuario instanceof Profesional) {
            return "profesional";
        } else {
            return "desconocido";
        }
    }
}
