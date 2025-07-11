package com.linkfix.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


import com.linkfix.entity.ServicioEntity;


public interface ServicioService {

    List<ServicioEntity> findAll();

    List<LocalTime> findHorasOcupadasPorFecha(Long idTecnico, LocalDate fecha); 

    Boolean solicitarServicio(Long idCliente, Long idTecnico, Long idElectrodomestico, LocalDateTime fecha_visita, String comentario );

    ServicioEntity findById(Long Id);

    Boolean tieneConflictoDeHorario(Long idTecnico, LocalDateTime inicioNuevo, LocalDateTime finNuevo);
}
