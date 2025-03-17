package org.example.fixerappbackend.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "administradores", schema = "fixer_app")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Administrador extends Usuario {
    @Column(name = "nivel_acceso", length = 50)
    private String nivelAcceso;

    @Column(name = "ultimo_acceso")
    private Instant ultimoAcceso;

    // Getters y setters
    public String getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(String nivelAcceso) { this.nivelAcceso = nivelAcceso; }
    public Instant getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(Instant ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
}