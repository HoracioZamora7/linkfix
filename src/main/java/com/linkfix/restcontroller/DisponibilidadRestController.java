package com.linkfix.restcontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkfix.dto.DisponibilidadDTO;
import com.linkfix.entity.DiaEntity;
import com.linkfix.entity.DisponibilidadEntity;
import com.linkfix.service.DisponibilidadService;
import com.linkfix.service.ServicioService;

@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadRestController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/horasDisponible")
    public List<String> obtenerHorasDisponiblesReales(Long idTecnico, LocalDate fecha) {
        int idDia = fecha.getDayOfWeek().getValue(); // Lunes = 1, etc.

        //Obtener bloques disponibles del técnico ese día
        List<DisponibilidadEntity> bloques = disponibilidadService.findByTecnicoIdAndDiaId(idTecnico, idDia);

        List<String> horasPosibles = new ArrayList<>();
        for (DisponibilidadEntity b : bloques) {
            horasPosibles.addAll(generarHorasDisponibles(b.getHoraInicio(), b.getHoraFin()));
        }

        //Obtener horas ya reservadas (fecha exacta) //crear metodo en servicio service para buscar esto. ademas de su query, claro
        List<LocalTime> horasOcupadas = servicioService.findHorasOcupadasPorFecha(idTecnico, fecha);

        //Filtro
        return horasPosibles.stream()
            .filter(hora -> !horasOcupadas.contains(LocalTime.parse(hora)))
            .collect(Collectors.toList());
    }



    //deberia mover esto a una clase estatica

    private List<String> generarHorasDisponibles(LocalTime inicio, LocalTime fin) {
        
        List<String> horas = new ArrayList<>();
        LocalTime actual = inicio;
        while (actual.isBefore(fin)) {
            horas.add(actual.toString()); //formato en HH y mm
            actual = actual.plusHours(1);
        }
        return horas;
    }

   @GetMapping("/verificar-conflicto")
    public boolean verificarConflicto( @RequestParam Long idTecnico, @RequestParam String fechaInicio, @RequestParam String fechaFin){
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio, formatter);
            LocalDateTime fin = LocalDateTime.parse(fechaFin, formatter);
            return servicioService.tieneConflictoDeHorario(idTecnico, inicio, fin);
        } catch (DateTimeParseException e) {
            
            return true;
        }
    }


}
