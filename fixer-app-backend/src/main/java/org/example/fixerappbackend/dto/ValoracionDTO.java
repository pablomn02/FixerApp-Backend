package org.example.fixerappbackend.dto;

import java.time.Instant;

public class ValoracionDTO {

    private Long id;
    private Long idCliente;
    private Long idProfesional;
    private Long idContratacion;
    private Long puntuacion;
    private String comentario;
    private Instant fechaTimestamp;
    private String nombreCliente;


    // Getters y setters


    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(Long idProfesional) {
        this.idProfesional = idProfesional;
    }

    public Long getIdContratacion() {
        return idContratacion;
    }

    public void setIdContratacion(Long idContratacion) {
        this.idContratacion = idContratacion;
    }

    public Long getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Long puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Instant getFechaTimestamp() {
        return fechaTimestamp;
    }

    public void setFechaTimestamp(Instant fechaTimestamp) {
        this.fechaTimestamp = fechaTimestamp;
    }
}
