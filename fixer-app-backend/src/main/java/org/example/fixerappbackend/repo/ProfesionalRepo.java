package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Profesional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfesionalRepo extends JpaRepository<Profesional, Long> {
    @Query("SELECT ps.profesional FROM ProfesionalServicio ps WHERE ps.servicio.id = :servicioId")
    List<Profesional> findProfesionalesByServicioId(@Param("servicioId") Long servicioId);
}
