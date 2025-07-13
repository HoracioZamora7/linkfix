package com.linkfix.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.ServicioEntity;

@Repository
public interface ServicioRepository extends JpaRepository<ServicioEntity, Long> {

    @Query("""
        SELECT s.fecha_visita, s.fecha_finalizacion
        FROM ServicioEntity s
        WHERE s.tecnico.id = :idTecnico
        AND DATE(s.fecha_visita) = :fecha
        AND s.estado.id = 9
    """) //solo con el estado "Aceptado, pero pendiente de atención" 
    List<Object[]> findHorasOcupadasPorFecha(@Param("idTecnico") Long idTecnico, @Param("fecha") LocalDate fecha); 


    //
    @Query(value = "SELECT * FROM servicio WHERE idTecnico = :idTecnico AND idEstado = 9 AND fecha_visita > :fechaReferencia ORDER BY fecha_visita ASC LIMIT 1", nativeQuery = true)
    Optional<LocalDateTime> findNextServicioHoraVisita(@Param("idTecnico") Long idTecnico, @Param("fechaReferencia") LocalDateTime fechaReferencia);

    @Query("SELECT s FROM ServicioEntity s WHERE s.usuario.id = :idUsuario AND s.estado.id IN :estados ORDER BY s.fecha_visita DESC")
    Page<ServicioEntity> findAllSolicitudesUsuario(@Param("idUsuario") Long idUsuario, @Param("estados") List<Integer> estados, Pageable pageable);

    @Query("SELECT s FROM ServicioEntity s WHERE s.tecnico.id = :idTecnico AND s.estado.id IN :estados ORDER BY s.fecha_visita DESC")
    Page<ServicioEntity> findAllPeticionesTecnico(@Param("idTecnico") Long idTecnico, @Param("estados") List<Integer> estados, Pageable pageable);

    
}
