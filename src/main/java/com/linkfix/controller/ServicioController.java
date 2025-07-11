package com.linkfix.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linkfix.controller.admin.AdminUsuarioController;
import com.linkfix.dto.DisponibilidadDTO;
import com.linkfix.dto.TecnicoListadoDTO;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.DiaEntity;
import com.linkfix.entity.DisponibilidadEntity;
import com.linkfix.entity.ServicioEntity;
import com.linkfix.service.DiaService;
import com.linkfix.service.DisponibilidadService;
import com.linkfix.service.ElectrodomesticoService;
import com.linkfix.service.ServicioService;
import com.linkfix.service.UsuarioService;
import static com.linkfix.util.SesionUtils.*;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/servicio")
@Controller
public class ServicioController {


    @Autowired
    private ElectrodomesticoService electrodomesticoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DiaService diaService;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/buscar")
    public String listarTecnicos(HttpSession session, Model model, RedirectAttributes redirectAttributes, 
    @RequestParam(required = false) Long idElectrodomestico, 
    @RequestParam(required = false) Integer idDia, 
    @RequestParam(required = false) String formHoraInicio, 
    @RequestParam(required = false) String formHoraFin,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size)
    {
        if(sesionIsValid(session) == false){
            return handleSesionInvalida(redirectAttributes);
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        
        LocalTime horaInicio = null;
        LocalTime horaFin = null;

        try {
            if (formHoraInicio != null && !formHoraInicio.isBlank()) {
                horaInicio = LocalTime.parse(formHoraInicio);
            }
            if (formHoraFin != null && !formHoraFin.isBlank()) {
                horaFin = LocalTime.parse(formHoraFin);
            }
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Formato de hora inválido.");
            return "redirect:/servicio/buscar";
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<TecnicoListadoDTO> listadoTecnicosDTO = usuarioService.listarTecnicosDisponibles(usuarioDTO.getUbigeo(), idElectrodomestico, idDia, horaInicio, horaFin, pageable);
        
        model.addAttribute("listaDias", diaService.findAll());
        model.addAttribute("listaElectrodomesticos", electrodomesticoService.findAll());
        model.addAttribute("listadoTecnicos", listadoTecnicosDTO.getContent());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", listadoTecnicosDTO.getTotalPages()); //paginas totale
        model.addAttribute("totalElements", listadoTecnicosDTO.getTotalElements()); //registros totales

        /*  */
        model.addAttribute("idElectrodomestico", idElectrodomestico);
        model.addAttribute("idDia", idDia);
        model.addAttribute("formHoraInicio", formHoraInicio);
        model.addAttribute("formHoraFin", formHoraFin);


        return "/servicio/listarTecnicos";
    }


    private static final Logger logger = LoggerFactory.getLogger(AdminUsuarioController.class);


    @PostMapping("/solicitar")
    public String mostrarFormularioSolicitud(HttpSession session, Model model, RedirectAttributes redirectAttributes, @RequestParam Long idTecnico, @RequestParam(required = false) Long idElectrodomestico){


        logger.info("/////////////////////////////////////////////////Hola, idTenico es: " + idTecnico.toString());


        if(sesionIsValid(session) == false){
            return handleSesionInvalida(redirectAttributes);
        }

        model.addAttribute("tecnico", usuarioService.toUsuarioDTOById(idTecnico));

        if(idElectrodomestico != null){ 
            model.addAttribute("electrodomestico", electrodomesticoService.findById(idElectrodomestico));
        }

        model.addAttribute("listaDisponibilidad", conseguirDisponibilidadHoraria(idTecnico));
        
        
        return "/servicio/formularioSolicitud";
    }


    @PostMapping("/solicitar/confirmar")
    public String confirmarSolicitud( 
        @RequestParam Long idTecnico, 
        @RequestParam(required = false) Long idElectrodomestico, 
        @RequestParam String fecha, 
        @RequestParam String hora, 
        @RequestParam(required = false) String comentario,
        HttpSession session,
        RedirectAttributes redirectAttributes,
        Model model
    ) 
    {
        if(sesionIsValid(session) == false){
            return handleSesionInvalida(redirectAttributes);
        }

        LocalDateTime fechaVisita = LocalDateTime.of(
            LocalDate.parse(fecha),
            LocalTime.parse(hora)
        );

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        if(!servicioService.solicitarServicio(usuarioDTO.getId(), idTecnico, idElectrodomestico, fechaVisita, comentario)){
            redirectAttributes.addFlashAttribute("error", "No se pudo crear la solicitud, inténtelo más tarde.");
            return "redirect:/servicio/buscar";
        }
        

        redirectAttributes.addFlashAttribute("mensaje", "Solicitud enviada correctamente.");
        return "redirect:/servicio/buscar";
    }


    @GetMapping("/confirmarSolicitud")
    public String mostrarDetalleSolicitud(HttpSession session, @RequestParam("servicio") Long servicioID, RedirectAttributes redirectAttributes, Model model){
        
        if(!sesionIsValid(session)){
            return handleSesionInvalida(redirectAttributes);
        }
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");

        ServicioEntity servicioEntity = servicioService.findById(servicioID);

        //si el usuario no es el tenico q está relacionado con el servicio
        if(usuarioDTO.getId() != servicioEntity.getTecnico().getId()){
            if(!isAdmin(session)) //y si no es admin
            {
                return handleErrorToIndex(redirectAttributes, "Unathorized!");
            }
        }

        boolean hayConflicto = servicioService.tieneConflictoDeHorario(servicioEntity.getTecnico().getId(), servicioEntity.getFecha_visita(), servicioEntity.getFecha_finalizacion());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String fechaInicioString = servicioEntity.getFecha_visita() != null ? servicioEntity.getFecha_visita().format(formatter) : "";
        String fechaFinString = servicioEntity.getFecha_finalizacion() != null ? servicioEntity.getFecha_finalizacion().format(formatter) : "";

        model.addAttribute("fechaInicioString", fechaInicioString);
        model.addAttribute("fechaFinString", fechaFinString);

        model.addAttribute("hayConflicto", hayConflicto);
        model.addAttribute("servicio", servicioEntity);

        return "/servicio/confirmarSolicitud";
    }

    





    //mover esto a un metodo estatico, si tengo tiempo
    private List<String> generarHorasDisponibles(LocalTime inicio, LocalTime fin) {
        
        List<String> horas = new ArrayList<>();
        LocalTime actual = inicio;
        while (actual.isBefore(fin)) {
            horas.add(actual.toString()); //formato en HH y mm
            actual = actual.plusHours(1);
        }
        return horas;
    }

    private List<DisponibilidadDTO> conseguirDisponibilidadHoraria(Long idTecnico){

        List<DisponibilidadDTO> disponibilidad = new ArrayList<>();

        for (DiaEntity dia : disponibilidadService.findDiasDisponiblesByTecnico(idTecnico)) {
            
            List<String> bloquesDisponibles = new ArrayList<>();
            for (DisponibilidadEntity intervalo : disponibilidadService.findByTecnicoIdAndDiaId(idTecnico, dia.getId())) {
                bloquesDisponibles.addAll(generarHorasDisponibles(intervalo.getHoraInicio(), intervalo.getHoraFin()));
            }

            DisponibilidadDTO disponibilidadDTO = new DisponibilidadDTO(dia.getId(), dia.getNombre(), bloquesDisponibles);
            disponibilidad.add(disponibilidadDTO);
        }
        logger.info(disponibilidad.toString());
        return disponibilidad;
    }

}
