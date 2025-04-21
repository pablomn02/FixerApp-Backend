package org.example.fixerappbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios", schema = "fixer_app")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nombre_usuario", unique = true)
    private String nombreUsuario;

    @Column(name = "contraseña")
    private String contrasena;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "valoracion")
    private Float valoracion;

    // Getters y setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getcontrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Float getValoracion() { return valoracion; }
    public void setValoracion(Float valoracion) { this.valoracion = valoracion; }
}