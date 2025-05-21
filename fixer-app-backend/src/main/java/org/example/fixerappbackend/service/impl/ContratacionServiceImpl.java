
package org.example.fixerappbackend.service.impl;

import org.example.fixerappbackend.dto.ContratacionCreateRequest;
import org.example.fixerappbackend.model.*;
import org.example.fixerappbackend.repo.ContratacionRepo;
import org.example.fixerappbackend.repo.HoraOcupadaRepo;
import org.example.fixerappbackend.service.ClienteService;
import org.example.fixerappbackend.service.ContratacionService;
import org.example.fixerappbackend.service.ProfesionalServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
        LocalDate fecha = fechaHoraUTC.toLocalDate();
        LocalTime horaInicio = fechaHoraUTC.toLocalTime();
        LocalTime horaFin = horaInicio.plusHours(1);

        List<HoraOcupada> ocupadas = horaOcupadaRepo.findByProfesionalServicioIdAndFecha(profesionalServicio.getId(), fecha);

        boolean solapado = ocupadas.stream().anyMatch(h ->
                horaInicio.isBefore(h.getHoraFin()) && horaFin.isAfter(h.getHoraInicio())
        );

        if (solapado) {
            throw new RuntimeException("El bloque ya está ocupado.");
        }
    }

    @Override
    public ResponseEntity<?> crearContratacion(ContratacionCreateRequest request) {
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

            this.validarDisponibilidad(profesionalServicio, request.getFechaHora(), 60);

            Contratacion nueva = new Contratacion();
            nueva.setCliente(cliente);
            nueva.setProfesionalServicio(profesionalServicio);
            nueva.setFechaHora(request.getFechaHora());
            nueva.setDuracionEstimada(60);
            nueva.setCostoTotal(request.getCostoTotal());
            nueva.setEstadoContratacion(EstadoContratacion.PENDIENTE);
            contratacionRepo.save(nueva);

            HoraOcupada bloque = new HoraOcupada();
            bloque.setFecha(request.getFechaHora().toLocalDate());
            bloque.setHoraInicio(request.getFechaHora().toLocalTime());
            bloque.setHoraFin(request.getFechaHora().toLocalTime().plusHours(1));
            bloque.setEstado("ocupado");
            bloque.setProfesionalServicio(profesionalServicio);
            horaOcupadaRepo.save(bloque);

            return ResponseEntity.ok(nueva);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @Override
    public List<String> getHorasOcupadas(Long idProfesionalServicio, String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        List<HoraOcupada> ocupadas = horaOcupadaRepo.findByProfesionalServicioIdAndFecha(idProfesionalServicio, fecha);
        return ocupadas.stream()
                .map(h -> h.getHoraInicio().toString().substring(0, 5))
                .toList();
    }

    @Override
    public List<LocalTime> getBloquesDisponibles(Long idProfesionalServicio, LocalDate fecha) {
        LocalTime inicio = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(17, 0);

        List<HoraOcupada> ocupadas = horaOcupadaRepo.findByProfesionalServicioIdAndFecha(idProfesionalServicio, fecha);
        List<LocalTime> disponibles = new ArrayList<>();
        LocalTime actual = inicio;

        while (!actual.plusHours(1).isAfter(fin)) {
            final LocalTime inicioBloque = actual;
            final LocalTime finBloque = actual.plusHours(1);

            boolean solapado = ocupadas.stream().anyMatch(h ->
                    inicioBloque.isBefore(h.getHoraFin()) && finBloque.isAfter(h.getHoraInicio())
            );

            if (!solapado) {
                disponibles.add(inicioBloque);
            }

            actual = actual.plusHours(1);
        }

        return disponibles;
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
        Contratacion c = contratacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contratación no encontrada"));
        c.setEstadoContratacion(estadoContratacion);
        contratacionRepo.save(c);
    }
}
