package org.example.fixerappbackend.dto;


public class RegisterRequest {
    private String nombre;
    private String username;
    private String contrasena;
    private String email;
    private Float valoracion;
    private String rol;


    public RegisterRequest() {
    }

    public RegisterRequest(String nombre, String username, String contrasena, String email, Float valoracion, String rol) {
        this.nombre = nombre;
        this.username = username;
        this.contrasena = contrasena;
        this.email = email;
        this.valoracion = valoracion;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getValoracion() {
        return valoracion;
    }

    public void setValoracion(Float valoracion) {
        this.valoracion = valoracion;
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
}
