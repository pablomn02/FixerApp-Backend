package org.example.fixerappbackend.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "clientes", schema = "fixer_app")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Cliente extends Usuario {
    @Column(name = "preferencias")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> preferencias;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Contratacion> contrataciones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();


    // Getters y setters
    public Map<String, Object> getPreferencias() { return preferencias; }
    public void setPreferencias(Map<String, Object> preferencias) { this.preferencias = preferencias; }
    public Set<Contratacion> getContrataciones() { return contrataciones; }
    public void setContrataciones(Set<Contratacion> contrataciones) { this.contrataciones = contrataciones; }
    public Set<Valoracion> getValoraciones() { return valoraciones; }
    public void setValoraciones(Set<Valoracion> valoraciones) { this.valoraciones = valoraciones; }
}