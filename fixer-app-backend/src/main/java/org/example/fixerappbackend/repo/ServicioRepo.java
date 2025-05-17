package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioRepo extends JpaRepository<Servicio, Long> {

    @Query("SELECT s FROM Servicio s WHERE s.idCategoria.id = :idCategoria")
    List<Servicio> findServiciosByCategoriaId(Integer idCategoria);
}
