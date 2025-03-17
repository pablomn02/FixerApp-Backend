package org.example.fixerappbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "valoraciones", schema = "fixer_app")
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracion")
    private Integer id;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_profesional")
    private Integer idProfesional;

    @Column(name = "puntuacion")
    private Integer puntuacion;

    @Lob
    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "fecha_timestamp")
    private LocalDateTime fechaTimestamp;

    @Column(name = "id_contratacion")
    private Integer idContratacion;

    @Column(name = "titulo")
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "id_cliente", insertable = false, updatable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_profesional", insertable = false, updatable = false)
    private Profesional profesional;

    @ManyToOne
    @JoinColumn(name = "id_contratacion", insertable = false, updatable = false)
    private Contratacion contratacion;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(Integer idProfesional) {
        this.idProfesional = idProfesional;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaTimestamp() {
        return fechaTimestamp;
    }

    public void setFechaTimestamp(LocalDateTime fechaTimestamp) {
        this.fechaTimestamp = fechaTimestamp;
    }

    public Integer getIdContratacion() {
        return idContratacion;
    }

    public void setIdContratacion(Integer idContratacion) {
        this.idContratacion = idContratacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Profesional getProfesional() {
        return profesional;
    }

    public void setProfesional(Profesional profesional) {
        this.profesional = profesional;
    }

    public Contratacion getContratacion() {
        return contratacion;
    }

    public void setContratacion(Contratacion contratacion) {
        this.contratacion = contratacion;
    }
}