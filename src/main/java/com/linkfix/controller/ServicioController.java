package com.linkfix.controller;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linkfix.dto.TecnicoListadoDTO;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.service.DiaService;
import com.linkfix.service.ElectrodomesticoService;
import com.linkfix.service.UsuarioService;

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

    @GetMapping("/buscar")
    public String listarTecnicos(HttpSession session, Model model, RedirectAttributes redirectAttributes, 
    @RequestParam(required = false) Long idElectrodomestico, 
    @RequestParam(required = false) Integer idDia, 
    @RequestParam(required = false) String formHoraInicio, 
    @RequestParam(required = false) String formHoraFin,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size)
    {

        if (session.getAttribute("logueado") == null){
            redirectAttributes.addFlashAttribute("error", "Sesión inválida");
            return "redirect:/index";//sesion no valida
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

}
