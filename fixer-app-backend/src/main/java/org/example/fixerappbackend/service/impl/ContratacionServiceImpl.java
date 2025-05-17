package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.repo.ContratacionRepo;
import org.example.fixerappbackend.service.ContratacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ContratacionServiceImpl implements ContratacionService {

    @Autowired
    private ContratacionRepo contratacionRepo;

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

        // 🔥 Convertimos correctamente de UTC a horario Madrid
        ZonedDateTime fechaHoraMadrid = fechaHoraUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Madrid"));
        LocalDateTime fechaHoraLocal = fechaHoraMadrid.toLocalDateTime();

        String diaSemana = fechaHoraLocal.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, new Locale("es", "ES"))
                .toLowerCase();

        Object rangos = horarioDisponible.get(diaSemana);
        if (rangos == null || rangos.toString().equalsIgnoreCase("null")) {
            throw new RuntimeException("El profesional no trabaja el día: " + diaSemana);
        }

        if (!(rangos instanceof List<?> lista)) {
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
                    return !horaSolicitada.isBefore(inicio) && !horaSolicitada.isAfter(fin);
                }
            }
            return false;
        });

        if (!disponible) {
            throw new RuntimeException("El profesional no está disponible en el horario solicitado.");
        }
    }

    @Override
    public List<String> getHorasOcupadas(Long idProfesionalServicio, String fecha) {
        LocalDate localDate = LocalDate.parse(fecha);
        List<Contratacion> contrataciones = contratacionRepo.findByProfesionalServicioIdAndFecha(idProfesionalServicio, localDate);

        return contrataciones.stream()
                .map(c -> c.getFechaHora().toLocalTime().toString().substring(0, 5))
                .toList();
    }
}
