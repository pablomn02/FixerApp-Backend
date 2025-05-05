package org.example.fixerappbackend.dto;

import java.math.BigDecimal;
import java.util.Map;

public class UpdateUsuarioDTO {
    public String nombre;
    public String nombreUsuario;
    public String contrasena;
    public String email;
    public Float valoracion;
    public Map<String, Object> preferencias;
    public String especialidad;
    public BigDecimal precioHora;
    public Map<String, Object> horarioDisponible;
    public Integer experiencia;
    public String certificaciones;
    public Map<String, Double> ubicacion;
}
