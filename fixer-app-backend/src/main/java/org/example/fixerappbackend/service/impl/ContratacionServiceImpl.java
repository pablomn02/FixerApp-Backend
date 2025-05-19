package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.*;
import org.example.fixerappbackend.repo.ContratacionRepo;
import org.example.fixerappbackend.repo.HoraOcupadaRepo;
import org.example.fixerappbackend.service.ClienteService;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ContratacionServiceImpl implements ContratacionService {

    @Autowired
    private ContratacionRepo contratacionRepo;

    @Autowired
    private ProfesionalServicioService profesionalServicioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private HoraOcupadaRepo horaOcupadaRepo;

    @Override
    public List<Contratacion> getAll() {
        return contratacionRepo.findAll();
    }

    @Override
    public Contratacion save(Contratacion contratacion) {
        return contratacionRepo.save(contratacion);
    }

    @Override
    public void validarDisponibilidad(ProfesionalServicio profesionalServicio, LocalDateTime fechaHoraUTC, int duracion) {
        Profesional profesional = profesionalServicio.getProfesional();
        Map<String, Object> horarioDisponible = profesional.getHorarioDisponible();
        if (horarioDisponible == null) {
            throw new RuntimeException("El profesional no tiene un horario de disponibilidad configurado.");
        }

        ZonedDateTime fechaHoraMadrid = fechaHoraUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Madrid"));
        LocalDateTime fechaHoraLocal = fechaHoraMadrid.toLocalDateTime();
        LocalDate fechaLocal = fechaHoraLocal.toLocalDate();
        LocalTime horaInicio = fechaHoraLocal.toLocalTime();
        LocalTime horaFin = horaInicio.plusMinutes(duracion);

        String diaSemana = fechaHoraLocal.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"))
                .toLowerCase();

        Object rangos = horarioDisponible.get(diaSemana);
        if (!(rangos instanceof List<?> lista)) throw new RuntimeException("Formato horario inválido");

        boolean enRango = lista.stream().anyMatch(r -> {
            if (r instanceof Map<?, ?> rango) {
                try {
                    LocalDateTime inicioDT = LocalDateTime.parse((String) rango.get("inicio"));
                    LocalDateTime finDT = LocalDateTime.parse((String) rango.get("fin"));
                    LocalTime inicio = inicioDT.toLocalTime();
                    LocalTime fin = finDT.toLocalTime();
                    return !horaInicio.isBefore(inicio) && !horaFin.isAfter(fin);
                } catch (Exception e) {
                    System.err.println("Error parseando horario: " + rango);
                    return false;
                }
            }
            return false;
        });

        if (!enRango) {
            throw new RuntimeException("El profesional no está disponible en el horario solicitado.");
        }

        boolean solapada = horaOcupadaRepo.existsSolapamiento(
                profesionalServicio, fechaLocal, horaInicio, horaFin
        );

        if (solapada) {
            throw new RuntimeException("El horario solicitado ya está ocupado.");
        }

        System.out.println("Hora disponible confirmada.");
    }

    @Override
    public List<String> getHorasOcupadas(Long idProfesionalServicio, String fecha) {
        LocalDate localDate = LocalDate.parse(fecha);
        List<Contratacion> contrataciones = contratacionRepo.findByProfesionalServicioIdAndFecha(idProfesionalServicio, localDate);

        return contrataciones.stream()
                .map(c -> c.getFechaHora()
                        .atZone(ZoneId.of("UTC"))
                        .withZoneSameInstant(ZoneId.of("Europe/Madrid"))
                        .toLocalTime()
                        .toString().substring(0, 5))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> crearContratacion(@RequestBody ContratacionCreateRequest request) {
        try {
            if (request.getIdUsuario() == null || request.getIdProfesionalServicio() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Faltan datos obligatorios",
                        "message", "Se requiere idUsuario e idProfesionalServicio"
                ));
            }

            ProfesionalServicio profesionalServicio = profesionalServicioService
                    .findById(request.getIdProfesionalServicio())
                    .orElseThrow(() -> new RuntimeException("ProfesionalServicio no encontrado."));

            Cliente cliente = clienteService
                    .findById(request.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("El usuario no es un cliente válido."));

            this.validarDisponibilidad(profesionalServicio, request.getFechaHora(), request.getDuracionEstimada());

            Contratacion nueva = new Contratacion();
            nueva.setCliente(cliente);
            nueva.setProfesionalServicio(profesionalServicio);
            nueva.setFechaHora(request.getFechaHora());
            nueva.setDuracionEstimada(request.getDuracionEstimada());
            nueva.setCostoTotal(request.getCostoTotal());
            nueva.setEstadoContratacion(EstadoContratacion.PENDIENTE);

            Contratacion guardada = this.save(nueva);

            ZonedDateTime zonedMadrid = request.getFechaHora().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Madrid"));
            HoraOcupada bloqueada = new HoraOcupada();
            bloqueada.setProfesionalServicio(profesionalServicio);
            bloqueada.setFecha(zonedMadrid.toLocalDate());
            bloqueada.setHoraInicio(zonedMadrid.toLocalTime());
            bloqueada.setHoraFin(zonedMadrid.toLocalTime().plusMinutes(request.getDuracionEstimada()));
            bloqueada.setEstado("ocupado");

            horaOcupadaRepo.save(bloqueada);

            return ResponseEntity.ok(ContratacionDTO.fromEntity(guardada));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
            ));
        }
    }

    @Override
    public List<Contratacion> findByClienteIdAndEstadoIn(Long idCliente, List<EstadoContratacion> estados) {
        return contratacionRepo.findByClienteIdAndEstadoContratacionIn(idCliente, estados);
    }

    @Override
    public List<Contratacion> findByProfesionalId(Long id) {
        return contratacionRepo.findByProfesionalServicio_Profesional_Id(id);
    }

    @Override
    public void actualizarEstado(Long id, EstadoContratacion estadoContratacion) {
        Contratacion contratacion = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contratación no encontrada con ID: " + id));

        contratacion.setEstadoContratacion(estadoContratacion);
        contratacionRepo.save(contratacion);
    }

    // Método auxiliar si necesitas actualizar por string (opcional)
    public void actualizarEstado(Long id, String nuevoEstado) {
        try {
            EstadoContratacion estado = EstadoContratacion.valueOf(nuevoEstado.toUpperCase());
            actualizarEstado(id, estado);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado);
        }
    }
}
