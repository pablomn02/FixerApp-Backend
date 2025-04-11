package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.RegisterRequest;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.Usuario;
import org.example.fixerappbackend.model.PasswordResetToken;
import org.example.fixerappbackend.repo.PasswordResetTokenRepo;
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
        if (!passwordEncoder.matches(loginRequest.getContrasena(), usuario.getcontrasena())) {
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

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        LOGGER.info("Intentando registrar usuario con email: " + registerRequest.getEmail());

        if (usuarioRepository.findByEmail(registerRequest.getEmail()) != null) {
            LOGGER.warning("El email ya está registrado: " + registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El email ya está registrado"));
        }

        if (registerRequest.getContrasena() == null || registerRequest.getContrasena().isEmpty()) {
            LOGGER.warning("La contraseña no puede ser nula o vacía");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "La contraseña es requerida"));
        }

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

        usuario.setNombre(registerRequest.getNombre());
        usuario.setNombreUsuario(registerRequest.getusuario());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setContrasena(passwordEncoder.encode(registerRequest.getContrasena()));
        usuario.setValoracion(0.0f);

        usuarioRepository.save(usuario);
        LOGGER.info("Usuario registrado exitosamente: " + usuario.getEmail());

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

    @Override
    @Transactional
    public void requestPasswordReset(String email) throws Exception {
        LOGGER.info("Solicitando recuperación de contraseña para: " + email);

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            LOGGER.warning("El correo no está registrado: " + email);
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
        LOGGER.info("Intentando restablecer contraseña con token: " + token);

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Token inválido o no encontrado."));

        if (resetToken.isExpired()) {
            LOGGER.warning("Token expirado para el usuario: " + resetToken.getUsuario().getEmail());
            throw new Exception("El token ha expirado.");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setContrasena(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);
        LOGGER.info("Contraseña restablecida para: " + usuario.getEmail());
    }

    private void sendPasswordResetEmail(String email, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String resetUrl = frontendUrl + "/reset-password?token=" + token;

        helper.setTo(email);
        helper.setSubject("Recuperación de Contraseña - FixerApp");
        helper.setText(
                "<div style=\"font-family: 'Roboto', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f5f8; border-radius: 10px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\">" +
                        "<h1 style=\"color: #3880ff; text-align: center; font-size: 24px; margin-bottom: 20px;\">Recuperación de Contraseña</h1>" +
                        "<p style=\"color: #333333; font-size: 16px; line-height: 1.5; margin-bottom: 20px;\">Hola,</p>" +
                        "<p style=\"color: #333333; font-size: 16px; line-height: 1.5; margin-bottom: 20px;\">Hemos recibido una solicitud para restablecer tu contraseña en <strong>FixerApp</strong>.</p>" +
                        "<p style=\"color: #333333; font-size: 16px; line-height: 1.5; margin-bottom: 20px;\">Haz clic en el siguiente botón para restablecer tu contraseña:</p>" +
                        "<div style=\"text-align: center; margin-bottom: 20px;\">" +
                        "<a href=\"" + resetUrl + "\" style=\"display: inline-block; padding: 12px 24px; background-color: #3880ff; color: #ffffff; text-decoration: none; border-radius: 5px; font-size: 16px; font-weight: bold;\">Restablecer Contraseña</a>" +
                        "</div>" +
                        "<p style=\"color: #333333; font-size: 16px; line-height: 1.5; margin-bottom: 20px;\">Este enlace es válido por 24 horas.</p>" +
                        "<p style=\"color: #333333; font-size: 16px; line-height: 1.5; margin-bottom: 20px;\">Si no solicitaste este cambio, ignora este correo.</p>" +
                        "<p style=\"color: #666666; font-size: 14px; text-align: center; margin-top: 30px;\">© 2025 FixerApp. Todos los derechos reservados.</p>" +
                        "</div>",
                true
        );

        mailSender.send(message);
        LOGGER.info("Correo de recuperación enviado a: " + email);
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