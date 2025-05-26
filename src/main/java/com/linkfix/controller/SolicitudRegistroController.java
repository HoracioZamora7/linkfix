package com.linkfix.controller;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.linkfix.entity.SolicitudRegistroEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.service.EstadoService;
import com.linkfix.service.SolicitudRegistroService;
import com.linkfix.service.UsuarioService;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudRegistroController {

    @Autowired
    private SolicitudRegistroService solicitudRegistroService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstadoService estadoService;

    private boolean isAdmin(HttpSession session) {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        return usuarioDTO != null && usuarioDTO.getRoles().contains(1); //si contiene el rol 1 (admin)
    }

    @GetMapping("/lista")
    public String listarSolicitudes(@RequestParam(required = false) String estado, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            model.addAttribute("error", "Funcion exclusiva para admins");
            return "redirect:/login"; //agregar mensaje de error y ocultar sessionid dela url

        }
        List<SolicitudRegistroEntity> solicitudes;
        /*filtro */
        if (estado == null) {
            solicitudes = solicitudRegistroService.findAll();
        } else {
            boolean estadoBoolean = Boolean.parseBoolean(estado);
            solicitudes = solicitudRegistroService.findAllByEstado(estadoBoolean);//revisar esto
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("estado", estado);//y esto
        return "solicitudes_de_registro/lista";
    }


    @GetMapping("/detalle/{id}")
    public String detalleSolicitud(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/home";
        }

        SolicitudRegistroEntity solicitud = solicitudRegistroService.findById(id);
        if (solicitud == null) {
            return "redirect:/solicitudes_de_registro/lista";
        }
        model.addAttribute("solicitud", solicitud);
        return "solicitudes_de_registro/detalle";  //crear carpeta para separar todos estos
    }


    @PostMapping("/editar/{id}")
    public String procesarComentario(@PathVariable Long id, @RequestParam("comentario") String comentario, @RequestParam(value = "aprobar",
    required = false) String aprobar, @RequestParam(value = "rechazar", required = false) String rechazar, HttpSession session) 
    {
        //
        if (!isAdmin(session)) {
            return "redirect:/home";
        } 
        else{
            UsuarioDTO adminDTO = (UsuarioDTO) session.getAttribute("logueado");
            
            UsuarioEntity admin = usuarioService.findById(adminDTO.getId());
            SolicitudRegistroEntity solicitud= solicitudRegistroService.findById(id);
            
            solicitud.setAdministrador(admin);
            solicitud.setComentario(comentario);

            UsuarioEntity tecnico = solicitud.getTecnico();
            if (aprobar != null) {
                
                tecnico.setEstado(estadoService.findById(1));//activo
            }

            if (rechazar != null) {
                tecnico.setEstado(estadoService.findById(4));//rechazado
            }

            usuarioService.update(tecnico);
            solicitudRegistroService.update(solicitud);
        }
        
        return "redirect:/solicitudes/lista";
    }

}
