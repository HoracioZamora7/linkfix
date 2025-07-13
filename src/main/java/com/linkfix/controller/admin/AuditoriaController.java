package com.linkfix.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.linkfix.controller.UsuarioController;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.aud.AUDUsuarioHistorial;
import com.linkfix.service.UsuarioRolService;
import com.linkfix.service.aud.AUDUsuarioHistorialService;
import static com.linkfix.util.SesionUtils.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/historial")
public class AuditoriaController {

    @Autowired
    private AUDUsuarioHistorialService historialService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @GetMapping("/usuarios")
    public String listarHistorialUsuarios(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam(value = "usuarioCorreo", required = false) String correo) {
        
        if(sesionIsValid(session) == false || isAdmin(session)==false){
            return handleSesionInvalida(redirectAttributes);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<AUDUsuarioHistorial> historialPage;

        
        if(correo==null || correo.trim().isEmpty()){
            historialPage = historialService.findAll(pageable);
        } 
        else{
            correo=correo.trim();
            //logger.info("correo: "+correo);
            historialPage = historialService.findByCorreo(correo, pageable);
            model.addAttribute("usuarioCorreo", correo);
            //logger.info(historialPage.getContent().toString());
        }

        for(AUDUsuarioHistorial elemento: historialPage.getContent()){
            StringBuilder nombreRoles = new StringBuilder();
            for(RolEntity rolEntity : usuarioRolService.findRolesbyIdUsuario(elemento.getIdUsuario()))
            {
                /* nombreRoles+= rolEntity.getNombre() + ", "; */
                nombreRoles.append(rolEntity.getNombre()).append(", ");
            }
            if (nombreRoles.length() > 2) {
                nombreRoles.setLength(nombreRoles.length() - 2);
            }

            elemento.setRoles(nombreRoles.toString());
        }
        
        model.addAttribute("historialPage", historialPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", historialPage.getTotalPages());
        model.addAttribute("totalElements", historialPage.getTotalElements()); 


        return "admin/auditoria/historialUsuarios";
    }

}
