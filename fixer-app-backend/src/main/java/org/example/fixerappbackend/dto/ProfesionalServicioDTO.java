package org.example.fixerappbackend.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ProfesionalServicioDTO {
    private Long idUsuario;
    private String nombre;
    private String especialidad;
    private BigDecimal precioHora;
    private Long idServicio;
    private String nombreServicio;
    private Long idProfesionalServicio;
    private Map<String, List<Map<String, String>>> horarioDisponible;

    private BigDecimal latitude;
    private BigDecimal longitude;

    public ProfesionalServicioDTO() {}

    public ProfesionalServicioDTO(Profesional profesional, ProfesionalServicio profesionalServicio) {
        this.idUsuario = profesional.getId();
        this.nombre = profesional.getNombre();
        this.especialidad = profesional.getEspecialidad();
        this.precioHora = profesional.getPrecioHora();
        this.idServicio = profesionalServicio.getServicio().getId();
        this.nombreServicio = profesionalServicio.getServicio().getNombre();
        this.idProfesionalServicio = profesionalServicio.getId();
        this.latitude = profesional.getLatitude();
        this.longitude = profesional.getLongitude();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType stringType = mapper.getTypeFactory().constructType(String.class);
            JavaType mapStringStringType = mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class);
            JavaType listOfMapStringStringType = mapper.getTypeFactory().constructCollectionType(List.class, mapStringStringType);
            JavaType finalMapType = mapper.getTypeFactory().constructMapType(Map.class, stringType, listOfMapStringStringType);

            this.horarioDisponible = mapper.convertValue(
                    profesional.getHorarioDisponible(),
                    finalMapType
            );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.horarioDisponible = null;
        }
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public BigDecimal getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(BigDecimal precioHora) {
        this.precioHora = precioHora;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public Long getIdProfesionalServicio() {
        return idProfesionalServicio;
    }

    public void setIdProfesionalServicio(Long idProfesionalServicio) {
        this.idProfesionalServicio = idProfesionalServicio;
    }

    public Map<String, List<Map<String, String>>> getHorarioDisponible() {
        return horarioDisponible;
    }

    public void setHorarioDisponible(Map<String, List<Map<String, String>>> horarioDisponible) {
        this.horarioDisponible = horarioDisponible;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
