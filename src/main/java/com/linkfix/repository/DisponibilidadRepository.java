package com.linkfix.repository;

import java.util.Optional;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.DiaEntity;
import com.linkfix.entity.DisponibilidadEntity;

@Repository
public interface DisponibilidadRepository extends JpaRepository<DisponibilidadEntity, Long> {

    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.usuario.id = :idTecnico")
    Optional<List<DisponibilidadEntity>> findByIdTecnico(@Param("idTecnico") Long id);    
    
    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.dia.id = :idDia")
    Optional<List<DisponibilidadEntity>> findByDia(@Param("idDia") Integer id);

    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.dia.id = :idDia AND d.horaInicio <= :horaFin AND d.horaFin >= :horaInicio AND d.usuario.id = :idUsuario")
    Optional<List<DisponibilidadEntity>> revisarHorario(@Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin, @Param("idDia") Integer idDia, @Param("idUsuario") Long idUsuario);

    @Query("SELECT DISTINCT d.dia FROM DisponibilidadEntity d WHERE d.usuario.id = :idTecnico")
    List<DiaEntity> findDiasDisponiblesByTecnico(@Param("idTecnico") Long idTecnico);

    @Query("SELECT d FROM DisponibilidadEntity d WHERE d.usuario.id= :idTecnico AND d.dia.id= :idDia ORDER BY d.horaInicio ")
    List<DisponibilidadEntity> findByTecnicoIdAndDiaId(@Param("idTecnico") Long idTecnico, @Param("idDia") int idDia);
}
