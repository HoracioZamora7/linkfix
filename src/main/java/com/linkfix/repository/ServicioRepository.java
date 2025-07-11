package com.linkfix.repository;

import java.time.LocalDate;
import java.util.List;

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

}
