package org.example.fixerappbackend.dto;

import org.example.fixerappbackend.model.Contratacion;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContratacionDTO {
    private Long idUsuario;
    private String nombreCliente;
    private Long idProfesionalServicio;
    private String nombreProfesional;
    private LocalDateTime fechaHora;
    private String estado;
    private Integer duracionEstimada;
    private BigDecimal costoTotal;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getIdProfesionalServicio() {
        return idProfesionalServicio;
    }

    public void setIdProfesionalServicio(Long idProfesionalServicio) {
        this.idProfesionalServicio = idProfesionalServicio;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public static ContratacionDTO fromEntity(Contratacion c) {
        ContratacionDTO dto = new ContratacionDTO();
        if (c.getCliente() != null) {
            dto.setIdUsuario(c.getCliente().getId());
            dto.setNombreCliente(c.getCliente().getNombre());
        }
        if (c.getProfesionalServicio() != null && c.getProfesionalServicio().getProfesional() != null) {
            dto.setIdProfesionalServicio(c.getProfesionalServicio().getId());
            dto.setNombreProfesional(c.getProfesionalServicio().getProfesional().getNombre());
        }
        dto.setFechaHora(c.getFechaHora());
        dto.setEstado(c.getEstado());
        dto.setDuracionEstimada(c.getDuracionEstimada());
        dto.setCostoTotal(c.getCostoTotal());
        return dto;
    }
}
