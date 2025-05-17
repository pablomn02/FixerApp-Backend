package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.example.fixerappbackend.repo.ContratacionRepo;
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

    @Override
    public List<Contratacion> getAll() {
        return contratacionRepo.findAll();
    }

    @Override
    public Contratacion save(Contratacion contratacion) {
        return contratacionRepo.save(contratacion);
    }

    @Override
    public void validarDisponibilidad(Profesional profesional, LocalDateTime fechaHoraUTC) {
        Map<String, Object> horarioDisponible = profesional.getHorarioDisponible();
        if (horarioDisponible == null) {
            throw new RuntimeException("El profesional no tiene un horario de disponibilidad configurado.");
        }

        ZonedDateTime fechaHoraUTCZoned = fechaHoraUTC.atZone(ZoneId.of("UTC"));
        ZonedDateTime fechaHoraMadrid = fechaHoraUTCZoned.withZoneSameInstant(ZoneId.of("Europe/Madrid"));
        LocalDateTime fechaHoraLocal = fechaHoraMadrid.toLocalDateTime();

        String diaSemana = fechaHoraLocal.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"))
                .toLowerCase();

        System.out.println("Día de la semana (local): " + diaSemana);
        System.out.println("Hora solicitada (local): " + fechaHoraLocal.toLocalTime());

        Object rangos = horarioDisponible.get(diaSemana);
        if (rangos == null || rangos.toString().equalsIgnoreCase("null")) {
            System.out.println("El profesional no trabaja el día: " + diaSemana);
            throw new RuntimeException("El profesional no trabaja el día: " + diaSemana);
        }

        if (!(rangos instanceof List<?> lista)) {
            System.out.println("Formato de horario inválido para el día: " + diaSemana);
            throw new RuntimeException("Formato de horario incorrecto para el día: " + diaSemana);
        }

        LocalTime horaSolicitada = fechaHoraLocal.toLocalTime();

        boolean disponible = lista.stream().anyMatch(r -> {
            if (r instanceof Map<?, ?> rango) {
                String inicioStr = (String) rango.get("inicio");
                String finStr = (String) rango.get("fin");
                if (inicioStr != null && finStr != null) {
                    LocalTime inicio = LocalTime.parse(inicioStr);
                    LocalTime fin = LocalTime.parse(finStr);
                    System.out.println("Comparando con rango: " + inicio + " - " + fin);
                    return !horaSolicitada.isBefore(inicio) && horaSolicitada.isBefore(fin.plusMinutes(1));
                }
            }
            return false;
        });

        if (!disponible) {
            System.out.println("Hora solicitada fuera del horario disponible.");
            throw new RuntimeException("El profesional no está disponible en el horario solicitado.");
        }

        System.out.println("Hora disponible confirmada.");
    }

    @Override
    public List<String> getHorasOcupadas(Long idProfesionalServicio, String fecha) {
        LocalDate localDate = LocalDate.parse(fecha);
        List<Contratacion> contrataciones = contratacionRepo.findByProfesionalServicioIdAndFecha(idProfesionalServicio, localDate);

        return contrataciones.stream()
                .map(c -> c.getFechaHora().toLocalTime().toString().substring(0, 5))
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

            System.out.println("ID Usuario: " + request.getIdUsuario());
            System.out.println("ID ProfesionalServicio: " + request.getIdProfesionalServicio());
            System.out.println("Fecha y hora (UTC): " + request.getFechaHora());

            ProfesionalServicio profesionalServicio = profesionalServicioService
                    .findById(request.getIdProfesionalServicio())
                    .orElseThrow(() -> new RuntimeException("ProfesionalServicio no encontrado."));

            Cliente cliente = clienteService
                    .findById(request.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("El usuario no es un cliente válido."));

            this.validarDisponibilidad(profesionalServicio.getProfesional(), request.getFechaHora());

            Contratacion nueva = new Contratacion();
            nueva.setCliente(cliente);
            nueva.setProfesionalServicio(profesionalServicio);
            nueva.setFechaHora(request.getFechaHora());
            nueva.setDuracionEstimada(request.getDuracionEstimada());
            nueva.setCostoTotal(request.getCostoTotal());
            nueva.setEstado("pendiente");

            Contratacion guardada = this.save(nueva);
            return ResponseEntity.ok(ContratacionDTO.fromEntity(guardada));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", e.getMessage()
            ));
        }
    }
}
