package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.ClienteRegisterRequest;
import org.example.fixerappbackend.dto.LoginRequest;
import org.example.fixerappbackend.dto.ProfesionalRegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginRequest loginRequest);
    ResponseEntity<?> registerCliente(ClienteRegisterRequest request);
    ResponseEntity<?> registerProfesional(ProfesionalRegisterRequest request);
    void requestPasswordReset(String email) throws Exception;
    void resetPassword(String token, String newPassword) throws Exception;
}
