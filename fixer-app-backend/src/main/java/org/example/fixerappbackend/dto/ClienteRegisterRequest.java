package org.example.fixerappbackend.dto;

import java.util.Map;

public class ClienteRegisterRequest {
    private String nombre;
    private String usuario;
    private String email;
    private String contrasena;
    private Map<String, Object> preferencias;

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Map<String, Object> getPreferencias() { return preferencias; }
    public void setPreferencias(Map<String, Object> preferencias) { this.preferencias = preferencias; }
}