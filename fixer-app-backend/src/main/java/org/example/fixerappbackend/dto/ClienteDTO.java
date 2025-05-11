package org.example.fixerappbackend.dto;

import java.util.Map;

public class ClienteDTO {
    private Integer id;
    private String nombre;
    private String preferencias;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    // Getters y setters
}
