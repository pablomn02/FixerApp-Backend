package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Profesional;
import org.example.fixerappbackend.model.ProfesionalServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfesionalRepo extends JpaRepository<Profesional, Long> {

    @Query("SELECT ps.profesional FROM ProfesionalServicio ps WHERE ps.servicio.id = :servicioId")
    List<Profesional> findProfesionalesByServicioId(@Param("servicioId") Long servicioId);

    @Query("SELECT ps FROM ProfesionalServicio ps WHERE ps.profesional.id = :profesionalId")
    List<ProfesionalServicio> findProfesionalServiciosByProfesionalId(@Param("profesionalId") Long profesionalId);

    @Query("SELECT ps FROM ProfesionalServicio ps " +
            "WHERE ps.profesional.id = :profesionalId AND ps.servicio.id = :servicioId")
    Optional<ProfesionalServicio> findByProfesional_IdAndServicio_Id(
            @Param("profesionalId") Long profesionalId,
            @Param("servicioId") Long servicioId);
}
