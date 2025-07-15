package com.linkfix.service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linkfix.entity.ServicioEntity;


public interface ServicioService {

    /* crear */
    Boolean solicitarServicio(Long idCliente, Long idTecnico, Long idElectrodomestico, LocalDateTime fecha_visita, String comentario );
    
    /* fetch */
    ServicioEntity findById(Long Id);
    List<ServicioEntity> findAll();
    
    /* horario  */
    Boolean tieneConflictoDeHorario(Long idTecnico, LocalDateTime inicioNuevo, LocalDateTime finNuevo);
    Optional<LocalDateTime> findNextServicioHoraVisita(Long idTecnico, LocalDateTime fechaReferencia);
    List<LocalTime> findHorasOcupadasPorFecha(Long idTecnico, LocalDate fecha); 

    /* resolucion de la solicitud */
    Boolean aceptarSolicitud(Long idServicio, LocalDateTime fechaFinalizacion );
    Boolean rechazarSolicitud(Long idServicio, String motivo );

    /* historial */

    Page<ServicioEntity> findAllSolicitudesUsuario(Long idUsuario, List<Integer> estados, Pageable pageable);

    Page<ServicioEntity> findAllPeticionesTecnico(Long idTecnico, List<Integer> estados, Pageable pageable);

    void calificarSolicitud(Long idServicio, Integer calificacion);

    boolean completarServicio(Long id);


}
