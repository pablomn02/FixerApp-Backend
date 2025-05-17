package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.HoraOcupada;
import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface HoraOcupadaRepo extends JpaRepository<HoraOcupada, Long> {
    @Query("SELECT COUNT(h) > 0 FROM HoraOcupada h " +
            "WHERE h.profesionalServicio = :profesionalServicio " +
            "AND h.fecha = :fecha " +
            "AND ((:inicio < h.horaFin) AND (:fin > h.horaInicio))")
    boolean existsSolapamiento(@Param("profesionalServicio") ProfesionalServicio profesionalServicio,
                               @Param("fecha") LocalDate fecha,
                               @Param("inicio") LocalTime inicio,
                               @Param("fin") LocalTime fin);

}
