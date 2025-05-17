package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Contratacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratacionRepo extends JpaRepository<Contratacion, Long> {
    @Query("SELECT c FROM Contratacion c WHERE c.profesionalServicio.id = :idProfesionalServicio AND DATE(c.fechaHora) = :fecha")
    List<Contratacion> findByProfesionalServicioIdAndFecha(@Param("idProfesionalServicio") Long idProfesionalServicio, @Param("fecha") LocalDate fecha);

}
