package org.example.fixerappbackend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContratacionCreateRequest {
    private Long idUsuario;
    private Long idProfesionalServicio;
    private LocalDate fechaHora;
    private Integer duracionEstimada;
    private BigDecimal costoTotal;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProfesionalServicio() {
        return idProfesionalServicio;
    }

    public void setIdProfesionalServicio(Long idProfesionalServicio) {
        this.idProfesionalServicio = idProfesionalServicio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora.atStartOfDay();
    }

    public void setFechaHora(LocalDate fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    // Getters y setters
}

