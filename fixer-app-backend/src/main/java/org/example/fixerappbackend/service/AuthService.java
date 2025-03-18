package org.example.fixerappbackend.service;

import org.example.fixerappbackend.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginRequest loginRequest);
}
