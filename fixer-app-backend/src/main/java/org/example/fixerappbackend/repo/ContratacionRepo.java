package org.example.fixerappbackend.repo;

import org.example.fixerappbackend.dto.ContratacionDTO;
import org.example.fixerappbackend.model.Cliente;
import org.example.fixerappbackend.model.Contratacion;
import org.example.fixerappbackend.model.EstadoContratacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratacionRepo extends JpaRepository<Contratacion, Long> {
    List<Contratacion> findByClienteIdAndEstadoContratacionIn(Long idCliente, List<EstadoContratacion> estados);

    List<Contratacion> findByProfesionalServicio_Profesional_Id(Long profesionalId);

    @Query(value = "SELECT * FROM fixer_app.contrataciones WHERE id_usuario = :clienteId", nativeQuery = true)
    List<Contratacion> findByClienteId(@Param("clienteId") Long clienteId);


}
