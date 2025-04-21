package org.example.fixerappbackend.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

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

    @Column(name = "ubicacion", columnDefinition = "GEOMETRY", nullable = false)
    @JsonIgnore
    private Point ubicacion;

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private Set<ProfesionalServicio> profesionalServicios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "profesional")
    @JsonIgnore
    private Set<Valoracion> valoraciones = new LinkedHashSet<>();

    // Método para inicializar el horario con todos los días vacíos
    public void inicializarHorario() {
        this.horarioDisponible = new LinkedHashMap<>();
        this.horarioDisponible.put("lunes", new ArrayList<>());
        this.horarioDisponible.put("martes", new ArrayList<>());
        this.horarioDisponible.put("miercoles", new ArrayList<>());
        this.horarioDisponible.put("jueves", new ArrayList<>());
        this.horarioDisponible.put("viernes", new ArrayList<>());
        this.horarioDisponible.put("sabado", new ArrayList<>());
        this.horarioDisponible.put("domingo", new ArrayList<>());
    }

    // Método para establecer un rango horario para un día específico
    @SuppressWarnings("unchecked")
    public void agregarRangoHorario(String dia, String inicio, String fin) {
        // Validar el formato de las horas (HH:mm)
        if (!inicio.matches("^([0-1][0-9]|2[0-3]):[0-5][0-9]$") || !fin.matches("^([0-1][0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new IllegalArgumentException("El formato de las horas debe ser HH:mm (24 horas)");
        }

        // Validar que el día sea válido
        List<String> diasValidos = Arrays.asList("lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo");
        if (!diasValidos.contains(dia.toLowerCase())) {
            throw new IllegalArgumentException("El día debe ser uno de: " + diasValidos);
        }

        // Si el horario no está inicializado, inicializarlo
        if (this.horarioDisponible == null) {
            inicializarHorario();
        }

        // Obtener la lista de rangos para el día
        List<Map<String, String>> rangos = (List<Map<String, String>>) this.horarioDisponible.get(dia.toLowerCase());

        // Crear el nuevo rango
        Map<String, String> rango = new LinkedHashMap<>();
        rango.put("inicio", inicio);
        rango.put("fin", fin);

        // Validar que el inicio sea antes que el fin
        if (!esHoraValida(inicio, fin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }

        // Añadir el rango a la lista
        rangos.add(rango);
    }

    // Método auxiliar para validar que la hora de inicio sea anterior a la hora de fin
    private boolean esHoraValida(String inicio, String fin) {
        String[] inicioPartes = inicio.split(":");
        String[] finPartes = fin.split(":");
        int inicioHoras = Integer.parseInt(inicioPartes[0]);
        int inicioMinutos = Integer.parseInt(inicioPartes[1]);
        int finHoras = Integer.parseInt(finPartes[0]);
        int finMinutos = Integer.parseInt(finPartes[1]);

        if (inicioHoras < finHoras) {
            return true;
        } else if (inicioHoras == finHoras) {
            return inicioMinutos < finMinutos;
        }
        return false;
    }

    // Método para establecer la ubicación desde el JSON
    @JsonSetter("ubicacion")
    public void setUbicacionFromJson(Map<String, Double> ubicacionMap) {
        if (ubicacionMap == null || !ubicacionMap.containsKey("latitud") || !ubicacionMap.containsKey("longitud")) {
            throw new IllegalArgumentException("El campo 'ubicacion' debe contener 'latitud' y 'longitud'");
        }
        double latitud = ubicacionMap.get("latitud");
        double longitud = ubicacionMap.get("longitud");
        setUbicacion(latitud, longitud);
    }

    // Método para establecer la ubicación
    public void setUbicacion(double latitud, double longitud) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(longitud, latitud));
        point.setSRID(4326);
        this.ubicacion = point;
    }

    // Método personalizado para serializar la ubicación como JSON
    @JsonGetter("ubicacion")
    public Map<String, Double> getUbicacionAsMap() {
        if (ubicacion == null) {
            return null;
        }
        return Map.of(
                "latitud", ubicacion.getY(),
                "longitud", ubicacion.getX()
        );
    }

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
    public Point getUbicacion() { return ubicacion; }
    public void setUbicacion(Point ubicacion) { this.ubicacion = ubicacion; }
    public Set<ProfesionalServicio> getProfesionalServicios() { return profesionalServicios; }
    public void setProfesionalServicios(Set<ProfesionalServicio> profesionalServicios) { this.profesionalServicios = profesionalServicios; }
    public Set<Valoracion> getValoraciones() { return valoraciones; }
    public void setValoraciones(Set<Valoracion> valoraciones) { this.valoraciones = valoraciones; }
}