package org.example.fixerappbackend.dto;

public class LoginRequest {
    private String email;
    private String contrasena;
    private String rol;

    public LoginRequest(String email, String contrasena, String rol) {
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public LoginRequest() {
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
