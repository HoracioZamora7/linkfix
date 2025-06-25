package com.linkfix.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.aud.AUDUsuarioHistorial;
import com.linkfix.service.UsuarioRolService;
import com.linkfix.service.aud.AUDUsuarioHistorialService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/historial/")
public class AuditoriaController {

    @Autowired
    private AUDUsuarioHistorialService historialService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @GetMapping("/usuarios")
    public String listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam(value = "usuarioCorreo", defaultValue = "") String correo) {
        
        if(sesionIsValid(session) == false || isAdmin(session)==false){
            return handleSesionInvalida(redirectAttributes);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<AUDUsuarioHistorial> historialPage;

        if(correo==""){
            historialPage = historialService.findAll(pageable);
            
        } 
        else{
            historialPage = historialService.findByCorreo(correo, pageable);
            model.addAttribute("usuarioCorreo", correo);
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


        return "auditoria/listaHistorialUsuarios";
    }

    //
    private boolean sesionIsValid(HttpSession httpSession)
    {
        return httpSession.getAttribute("logueado") != null; //si es null la sesion es invalida
    }

    private String handleSesionInvalida(RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("error", "Sesión inválida");
        return "redirect:/index";
    }

    private boolean isAdmin(HttpSession session) 
    {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        return usuarioDTO != null && usuarioDTO.getRoles().contains(1); //si contiene el rol 1 (admin)
    }
}
