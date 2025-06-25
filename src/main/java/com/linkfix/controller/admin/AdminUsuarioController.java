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

import com.linkfix.dto.ListadoUsuariosDTO;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.RolEntity;
import com.linkfix.service.UsuarioRolService;
import com.linkfix.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    private static final Logger logger = LoggerFactory.getLogger(AdminUsuarioController.class);

    @GetMapping("/usuarios")
    public String listarUsuarios(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam(value = "usuarioCorreo", required = false) String correo){
 
        if(sesionIsValid(session) == false || isAdmin(session)==false){
            return handleSesionInvalida(redirectAttributes);
        }    
        
        Pageable pageable = PageRequest.of(page, size);
        if(correo==null) correo="";
        Page<ListadoUsuariosDTO> usuarioPage = usuarioService.listarUsuarios(correo, pageable);
        model.addAttribute("usuarioCorreo", correo);
        logger.info("correo: "+correo);
        logger.info(usuarioPage.getContent().toString());
        
        for(ListadoUsuariosDTO elemento: usuarioPage.getContent()){
            StringBuilder nombreRoles = new StringBuilder();
            for(RolEntity rolEntity : usuarioRolService.findRolesbyIdUsuario(elemento.getId()))
            {
                /* nombreRoles+= rolEntity.getNombre() + ", "; */
                nombreRoles.append(rolEntity.getNombre()).append(", ");
            }
            if (nombreRoles.length() > 2) {
                nombreRoles.setLength(nombreRoles.length() - 2);
            }
            
            elemento.setRoles(nombreRoles.toString());
        }
        
        model.addAttribute("usuarioPage", usuarioPage);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", usuarioPage.getTotalPages());
        model.addAttribute("totalElements", usuarioPage.getTotalElements()); 

        return "/admin/listarUsuarios";
    }



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
