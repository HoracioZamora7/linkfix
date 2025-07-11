package com.linkfix.service;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import com.linkfix.entity.DiaEntity;
import com.linkfix.entity.DisponibilidadEntity;


public interface DisponibilidadService {

    List<DisponibilidadEntity> findAll();
    Optional<List<DisponibilidadEntity>> findByIdTecnico(Long id);
    Optional<List<DisponibilidadEntity>> findByDia(Integer id);
    Optional<List<DisponibilidadEntity>> revisarHorario(LocalTime horaInicio, LocalTime horaFin, Integer idDia, Long usuarioID);
    Boolean deleteById(Long id, Long usuarioID);
    Integer save(LocalTime horaInicio, LocalTime horaFin, Integer idDia, Long usuarioID);

    List<DiaEntity> findDiasDisponiblesByTecnico(Long idTecnico);
    List<DisponibilidadEntity> findByTecnicoIdAndDiaId(Long idTecnico, int idDia);
}
