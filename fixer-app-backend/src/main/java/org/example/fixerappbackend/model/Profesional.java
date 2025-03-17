package org.example.fixerappbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "profesionales", schema = "fixer_app")
public class Profesional {
    @Id
    @Column(name = "id_profesional", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_profesional", nullable = false)
    private Usuario usuario;

    @Column(name = "especialidad", length = 100)
    private String especialidad;

    @Column(name = "precio_hora", precision = 10, scale = 2)
    private BigDecimal precioHora;

    @Column(name = "horario_disponible")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> horarioDisponible;

    @Column(name = "experiencia")
    private Integer experiencia;

    @Lob
    @Column(name = "certificaciones", columnDefinition = "TEXT")
    private String certificaciones;

    @OneToMany(mappedBy = "profesional")
    private Set<ProfesionalServicio> profesionalServicios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProfesional")
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Map<String, Object> getHorarioDisponible() {
        return horarioDisponible;
    }

    public void setHorarioDisponible(Map<String, Object> horarioDisponible) {
        this.horarioDisponible = horarioDisponible;
    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public String getCertificaciones() {
        return certificaciones;
    }

    public void setCertificaciones(String certificaciones) {
        this.certificaciones = certificaciones;
    }

    public Set<ProfesionalServicio> getProfesionalServicios() {
        return profesionalServicios;
    }

    public void setProfesionalServicios(Set<ProfesionalServicio> profesionalServicios) {
        this.profesionalServicios = profesionalServicios;
    }

    public Set<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(Set<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }
}