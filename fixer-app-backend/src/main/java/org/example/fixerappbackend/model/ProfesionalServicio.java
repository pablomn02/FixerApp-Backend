package org.example.fixerappbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "profesional_servicios", schema = "fixer_app")
public class ProfesionalServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesional_servicio", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_profesional", insertable = false, updatable = false)
    private Profesional profesional;

    @ManyToOne
    @JoinColumn(name = "id_servicio", insertable = false, updatable = false)
    private Servicio servicio;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Lob
    @Column(name = "descripcion_servicio", columnDefinition = "TEXT")
    private String descripcionServicio;

    @OneToMany(mappedBy = "idProfesionalServicio")
    private Set<Contratacion> contrataciones = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public Set<Contratacion> getContrataciones() {
        return contrataciones;
    }

    public void setContrataciones(Set<Contratacion> contrataciones) {
        this.contrataciones = contrataciones;
    }
}