package org.example.fixerappbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "clientes", schema = "fixer_app")
public class Cliente {
    @Id
    @Column(name = "id_cliente", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "idCliente")
    private Set<Contratacion> contrataciones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCliente")
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuarios() {
        return usuario;
    }

    public void setUsuarios(Usuario usuarios) {
        this.usuario = usuarios;
    }

    public Set<Contratacion> getContrataciones() {
        return contrataciones;
    }

    public void setContrataciones(Set<Contratacion> contrataciones) {
        this.contrataciones = contrataciones;
    }

    public Set<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(Set<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }
}