// src/main/java/org/example/fixerappbackend/controller/AuthController.java
package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.ClienteRegisterRequest;
import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.ProfesionalRegisterRequest;
import org.example.fixerappbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@RequestBody ClienteRegisterRequest request) {
        return authService.registerCliente(request);
    }

    @PostMapping("/register/profesional")
    public ResponseEntity<?> registerProfesional(@RequestBody ProfesionalRegisterRequest request) {
        return authService.registerProfesional(request);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        try {
            authService.requestPasswordReset(email);
            return ResponseEntity.ok(Collections.singletonMap("message", "Se ha enviado un enlace de recuperación a tu correo electrónico."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        try {
            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña restablecida exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
