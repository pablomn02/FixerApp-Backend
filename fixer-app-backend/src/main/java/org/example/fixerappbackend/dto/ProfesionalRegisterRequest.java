package org.example.fixerappbackend.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ProfesionalRegisterRequest {
    private String nombre;
    private String usuario;
    private String email;
    private String contrasena;
    private String especialidad;
    private BigDecimal precioHora;
    private Map<String, Double> ubicacion;
    private Map<String, Object> horarioDisponible;
    private Integer experiencia;
    private String certificaciones;
    private Long idServicio; // 🚀 Nuevo campo agregado

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public BigDecimal getPrecioHora() { return precioHora; }
    public void setPrecioHora(BigDecimal precioHora) { this.precioHora = precioHora; }

    public Map<String, Double> getUbicacion() { return ubicacion; }
    public void setUbicacion(Map<String, Double> ubicacion) { this.ubicacion = ubicacion; }

    public Map<String, Object> getHorarioDisponible() { return horarioDisponible; }
    public void setHorarioDisponible(Map<String, Object> horarioDisponible) { this.horarioDisponible = horarioDisponible; }

    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }

    public String getCertificaciones() { return certificaciones; }
    public void setCertificaciones(String certificaciones) { this.certificaciones = certificaciones; }

    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }
}
