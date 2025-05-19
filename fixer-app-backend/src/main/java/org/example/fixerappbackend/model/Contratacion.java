package org.example.fixerappbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "contrataciones", schema = "fixer_app")
public class Contratacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contratacion", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonIgnoreProperties({"contrataciones", "valoraciones", "hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_profesional_servicio")
    @JsonIgnore
    private ProfesionalServicio profesionalServicio;

    @Column(name = "fecha_hora")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;


    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoContratacion estadoContratacion;

    @Column(name = "duracion_estimada")
    private Integer duracionEstimada;

    @Column(name = "costo_total", precision = 10, scale = 2)
    private BigDecimal costoTotal;

    @OneToMany(mappedBy = "contratacion")
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public ProfesionalServicio getProfesionalServicio() { return profesionalServicio; }
    public void setProfesionalServicio(ProfesionalServicio profesionalServicio) { this.profesionalServicio = profesionalServicio; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public EstadoContratacion getEstadoContratacion() {
        return estadoContratacion;
    }

    public void setEstadoContratacion(EstadoContratacion estadoContratacion) {
        this.estadoContratacion = estadoContratacion;
    }

    public Integer getDuracionEstimada() { return duracionEstimada; }
    public void setDuracionEstimada(Integer duracionEstimada) { this.duracionEstimada = duracionEstimada; }

    public BigDecimal getCostoTotal() { return costoTotal; }
    public void setCostoTotal(BigDecimal costoTotal) { this.costoTotal = costoTotal; }

    public Set<Valoracion> getValoraciones() { return valoraciones; }
    public void setValoraciones(Set<Valoracion> valoraciones) { this.valoraciones = valoraciones; }
    public void setUsuarioId(Long idUsuario) {
        if (this.cliente == null) {
            this.cliente = new Cliente();
        }
        this.cliente.setId(idUsuario);
    }
}
