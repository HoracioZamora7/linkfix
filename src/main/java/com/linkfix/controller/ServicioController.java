package com.linkfix.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        //logger.info("/////////////////////////////////////////////////Hola, idTenico es: " + idTecnico.toString());

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
    public String mostrarFormConfirmarSolicitud(HttpSession session, @RequestParam("servicio") Long servicioID, RedirectAttributes redirectAttributes, Model model){
        
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

        if(servicioEntity.getEstado().getId() != 8)
        {
            redirectAttributes.addFlashAttribute("error", "Solicitud ya resuelta");
            return "redirect:/servicio/historialSolicitudes";
        }
        //fecha de visita del servicio de reparación mas cercano a la solicitud
        String fechaLimite = servicioService.findNextServicioHoraVisita(servicioEntity.getId(), servicioEntity.getFecha_visita()).toString();

        model.addAttribute("fechaInicioString", servicioEntity.getFecha_visita().toString());
        model.addAttribute("fechaLimite", fechaLimite); 
        model.addAttribute("servicio", servicioEntity);

        return "servicio/confirmarSolicitud";
    }


    @PostMapping("/confirmar")
    public String confirmarSolicitud(@RequestParam Long idServicio, @RequestParam Integer duracionHoras, RedirectAttributes redirectAttributes, HttpSession session) {
        
        ServicioEntity servicioEntity = servicioService.findById(idServicio);
        LocalDateTime fechaFinal = servicioEntity.getFecha_visita().plusHours(duracionHoras);

        if (duracionHoras == null || duracionHoras < 1 || duracionHoras > 12) {
            redirectAttributes.addFlashAttribute("error", "La duración ingresada no es válida.");
            return "redirect:/servicio/confirmarSolicitud?servicio=" + idServicio;
        }
        
        if(!servicioService.aceptarSolicitud(idServicio, fechaFinal)) {
            redirectAttributes.addFlashAttribute("error", "No se pudo agregar");
        } else{
            redirectAttributes.addFlashAttribute("mensaje", "Guardado con éxito");
        }
        

        return "redirect:/servicio/historialPeticiones";
    }

    
    @PostMapping("/rechazar")
    public String rechazarSolicitud(@RequestParam Long idServicio, @RequestParam String motivo, RedirectAttributes redirectAttributes, HttpSession session) {

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }
        
        boolean exito = servicioService.rechazarSolicitud(idServicio, motivo);

        if (!exito) {
            redirectAttributes.addFlashAttribute("error", "No se pudo rechazar la solicitud. Contacta con soporte");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud rechazada correctamente.");
        }

        return "redirect:/servicio/historialPeticiones";
    }

    @GetMapping("/historialSolicitudes")
    public String historialSolicitudes(RedirectAttributes redirectAttributes, HttpSession session, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "7,8,9") List<Integer> estados) {

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes); 
        }

        int pageSize= 15;

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("fecha_visita").descending());

        Page<ServicioEntity> solicitudes = servicioService.findAllSolicitudesUsuario(usuarioDTO.getId(), estados, pageable);

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("estadosSeleccionados", estados);

        return "servicio/historial/solicitudes";
    }

    @GetMapping("/historialPeticiones")
    public String historialPeticiones(RedirectAttributes redirectAttributes, HttpSession session, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "7,8,9") List<Integer> estados) {

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes); 
        }

        int pageSize = 15;

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("fecha_visita").descending());

        Page<ServicioEntity> peticiones = servicioService.findAllPeticionesTecnico(usuarioDTO.getId(), estados, pageable);

        model.addAttribute("peticiones", peticiones);
        model.addAttribute("estadosSeleccionados", estados);

        return "servicio/historial/peticiones";
    }

    @GetMapping("/detalle")
    public String mostrarDetalleSolicitud(RedirectAttributes redirectAttributes, HttpSession session, Model model, @RequestParam("id") Long servicioID) {

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes); 
        } 
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");

        ServicioEntity servicioEntity = servicioService.findById(servicioID);

        Long userID = usuarioDTO.getId();

        if(userID != servicioEntity.getTecnico().getId() && userID != servicioEntity.getUsuario().getId()){
            if(!isAdmin(session)) //y si no es admin
            {
                return handleErrorToIndex(redirectAttributes, "Unathorized!");
            }
        }

        model.addAttribute("servicio", servicioEntity);

        return "servicio/detalle";
    }

    @PostMapping("/solicitudes/calificar")
    public String calificarServicio(@RequestParam("solicitudId") Long idServicio, @RequestParam("calificacion") Integer calificacion, RedirectAttributes redirectAttributes, HttpSession session) {

        System.out.println(">>> MÉTODO CALIFICAR INICIADO");
        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }

        try {
            if (calificacion < 1 || calificacion > 5) {
                redirectAttributes.addFlashAttribute("error", "Calificación inválida.");
                return "redirect:/servicio/historialSolicitudes";
            }

            // Aplicar tu lógica personalizada aquí
            servicioService.calificarSolicitud(idServicio, calificacion);
            logger.info("Desde controller: "+ calificacion.toString());

            redirectAttributes.addFlashAttribute("mensaje", "Servicio calificado exitosamente.");
            return "redirect:/servicio/historialSolicitudes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al calificar: " + e.getMessage());
            logger.error("Error al calificar", e);
            return "redirect:/home";
        }
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
