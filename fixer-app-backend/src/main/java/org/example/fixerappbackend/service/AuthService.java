package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ClienteRegisterRequest;
import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.ProfesionalRegisterRequest;
import org.example.fixerappbackend.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginRequest loginRequest);

    void requestPasswordReset(String email) throws Exception;

    void resetPassword(String token, String newPassword) throws Exception;

    ResponseEntity<?> registerCliente(ClienteRegisterRequest registerRequest);

    ResponseEntity<?> registerProfesional(ProfesionalRegisterRequest registerRequest);
}
