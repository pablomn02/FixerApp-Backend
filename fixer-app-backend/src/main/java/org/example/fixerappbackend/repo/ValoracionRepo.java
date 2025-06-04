package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.model.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValoracionRepo extends JpaRepository<Valoracion, Long> {
    List<Valoracion> findByProfesionalId(Long idProfesional);
}
