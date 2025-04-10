package org.example.fixerappbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private Set<ProfesionalServicio> profesionalServicios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();

    // Getters y setters
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
    public Set<ProfesionalServicio> getProfesionalServicios() { return profesionalServicios; }
    public void setProfesionalServicios(Set<ProfesionalServicio> profesionalServicios) { this.profesionalServicios = profesionalServicios; }
    public Set<Valoracion> getValoraciones() { return valoraciones; }
    public void setValoraciones(Set<Valoracion> valoraciones) { this.valoraciones = valoraciones; }
}