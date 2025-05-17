package org.example.fixerappbackend.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "profesionales", schema = "fixer_app")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Profesional extends Usuario {

    @Column(name = "especialidad", length = 100)
    private String especialidad;

    @Column(name = "precio_hora", precision = 10, scale = 2)
    private BigDecimal precioHora;

    @Column(name = "horario_disponible")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> horarioDisponible;

    @Column(name = "experiencia")
    private Integer experiencia;

    @Column(name = "certificaciones", columnDefinition = "TEXT")
    private String certificaciones;

    @Column(name = "calificacion_promedio")
    private Float calificacionPromedio;

    @Column(name = "total_contrataciones")
    private Integer totalContrataciones;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id")
    @JsonManagedReference
    private Servicio servicio;

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private Set<ProfesionalServicio> profesionalServicios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();

    // Getters y Setters

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public BigDecimal getPrecioHora() { return precioHora; }
    public void setPrecioHora(BigDecimal precioHora) { this.precioHora = precioHora; }

    public Map<String, Object> getHorarioDisponible() { return horarioDisponible; }
    public void setHorarioDisponible(Map<String, Object> horarioDisponible) { this.horarioDisponible = horarioDisponible; }

    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }

    public String getCertificaciones() { return certificaciones; }
    public void setCertificaciones(String certificaciones) { this.certificaciones = certificaciones; }

    public Float getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(Float calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }

    public Integer getTotalContrataciones() { return totalContrataciones; }
    public void setTotalContrataciones(Integer totalContrataciones) { this.totalContrataciones = totalContrataciones; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Set<ProfesionalServicio> getProfesionalServicios() { return profesionalServicios; }
    public void setProfesionalServicios(Set<ProfesionalServicio> profesionalServicios) { this.profesionalServicios = profesionalServicios; }

    public Set<Valoracion> getValoraciones() { return valoraciones; }
    public void setValoraciones(Set<Valoracion> valoraciones) { this.valoraciones = valoraciones; }

    @JsonGetter("ubicacion")
    public Map<String, BigDecimal> getUbicacion() {
        if (latitude == null || longitude == null) return null;
        return Map.of(
                "latitud", latitude,
                "longitud", longitude
        );
    }

    @JsonSetter("ubicacion")
    public void setUbicacion(Map<String, BigDecimal> ubicacion) {
        if (ubicacion == null || ubicacion.get("latitud") == null || ubicacion.get("longitud") == null) {
            throw new IllegalArgumentException("ubicacion debe contener latitud y longitud");
        }
        this.latitude = ubicacion.get("latitud");
        this.longitude = ubicacion.get("longitud");
    }
}
