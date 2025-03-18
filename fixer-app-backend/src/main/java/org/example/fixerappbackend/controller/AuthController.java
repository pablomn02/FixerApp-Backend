package org.example.fixerappbackend.controller;

import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.RegisterRequest;
import org.example.fixerappbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> resgister(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }
}