package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ClienteRegisterRequest;
import org.example.fixerappbackend.dto.ProfesionalRegisterRequest;
import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register/cliente")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> registerCliente(@RequestBody ClienteRegisterRequest registerRequest) {
        return authService.registerCliente(registerRequest);
    }

    @PostMapping("/register/profesional")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> registerProfesional(@RequestBody ProfesionalRegisterRequest registerRequest) {
        return authService.registerProfesional(registerRequest);
    }

    @PostMapping("/request-password-reset")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            String email = request.get("email");
            authService.requestPasswordReset(email);
            response.put("message", "Se ha enviado un enlace de recuperación a tu correo electrónico.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/reset-password")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            authService.resetPassword(token, newPassword);
            response.put("message", "Contraseña restablecida exitosamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
}