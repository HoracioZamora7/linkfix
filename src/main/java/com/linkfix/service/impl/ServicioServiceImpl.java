package com.linkfix.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.linkfix.entity.ServicioEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.repository.ServicioRepository;
import com.linkfix.service.ElectrodomesticoService;
import com.linkfix.service.EmailService;
import com.linkfix.service.EstadoService;
import com.linkfix.service.ServicioService;
import com.linkfix.service.UsuarioService;

import jakarta.mail.MessagingException;

@Service
public class ServicioServiceImpl implements ServicioService{

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private ElectrodomesticoService electrodomesticoService;

    @Autowired
    private EmailService emailService;

    @Value("${app.url.base}")
    private String appBaseUrl;

    @Override
    public List<ServicioEntity> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    public List<LocalTime> findHorasOcupadasPorFecha(Long idTecnico, LocalDate fecha) {
    List<Object[]> resultados = servicioRepository.findHorasOcupadasPorFecha(idTecnico, fecha);
    List<LocalTime> horasOcupadas = new ArrayList<>();

    for (Object[] fila : resultados) {
        LocalDateTime inicio = (LocalDateTime) fila[0];
        LocalDateTime fin = (LocalDateTime) fila[1];

        LocalTime actual = inicio.toLocalTime();
        while (actual.isBefore(fin.toLocalTime())) {
            horasOcupadas.add(actual);
            actual = actual.plusHours(1); //
        }
    }

    return horasOcupadas;
    }

    @Override
    public Boolean solicitarServicio(Long idCliente, Long idTecnico, Long idElectrodomestico,
            LocalDateTime fecha_visita, String comentario) {
                
        try {
            ServicioEntity servicioEntity = new ServicioEntity();
            UsuarioEntity tecnicoEntity = usuarioService.findById(idTecnico);
            servicioEntity.setUsuario(usuarioService.findById(idCliente));
            servicioEntity.setTecnico(tecnicoEntity);
            servicioEntity.setFecha_visita(fecha_visita);
            servicioEntity.setFecha_solicitud(LocalDateTime.now());
            servicioEntity.setEstado(estadoService.findById(8)); //pendiente de confirmacion
            servicioEntity.setComentario(comentario);
            
            
            if(idElectrodomestico!=null){
                servicioEntity.setElectrodomestico(electrodomesticoService.findById(idElectrodomestico));
            }

            
            //return servicioRepository.save(servicioEntity)!= null ? true : false;

            Long servicioId = servicioRepository.save(servicioEntity).getId();
            
            if (servicioId == null){
                return false;
            }

            /*  ARMAR CORREO*/
            //
            Map<String, String> variablesEmail = new HashMap<>();
            variablesEmail.put("username", tecnicoEntity.getPersona().getApellidos()+", " + tecnicoEntity.getPersona().getNombre());
            variablesEmail.put("enlaceDetalle", appBaseUrl+"/servicio/confirmarSolicitud?servicio="+servicioId);
            enviarEmail(tecnicoEntity.getCorreo(), "Alguien solicito tu servicio de reparación!", "templates/email/notificacionTecnico.html", variablesEmail);
            //
            /* ARMAR CORREO */


            return true;
        } catch (Exception e) {
            
            
            return false;
        }
        
    }


    @Override
    public ServicioEntity findById(Long Id) {
        return servicioRepository.findById(Id).orElse(null);
    }


        
    private void enviarEmail(String mailAddress, String asunto, String plantillaHtml, Map<String, String> variables)
    {
        try {
            emailService.sendHtmlEmail(mailAddress, asunto, plantillaHtml, variables);
        } catch (MessagingException e) {
            emailService.sendEmail(mailAddress, asunto, "Algo ocurrio, notifica al personal tecnico");
            e.printStackTrace();
        }
    }

    @Override
    public Boolean tieneConflictoDeHorario(Long idTecnico, LocalDateTime inicioNuevo, LocalDateTime finNuevo) {
        List<Object[]> resultados = servicioRepository.findHorasOcupadasPorFecha(idTecnico, inicioNuevo.toLocalDate());

        for (Object[] fila : resultados) {
            LocalDateTime inicioExistente = (LocalDateTime) fila[0];
            LocalDateTime finExistente = (LocalDateTime) fila[1];

            // Si hay conflict
            if (!(finNuevo.isBefore(inicioExistente) || inicioNuevo.isAfter(finExistente))) {
                return true;
            }
        }
        return false;
    }

}
