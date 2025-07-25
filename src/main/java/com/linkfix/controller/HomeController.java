package com.linkfix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.service.DepartamentoService;

import jakarta.servlet.http.HttpSession;
import static com.linkfix.util.SesionUtils.*;

@Controller
public class HomeController {

    /* @Autowired
    private UsuarioService service; */

    @Autowired
    private DepartamentoService deptService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model)
    { 
        UsuarioEntity usuario = new UsuarioEntity();
        model.addAttribute("usuario", usuario);
        model.addAttribute("departamentos", deptService.findAll());
        return "registro";
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model)
    { 
        return "index";
    }
    
    @GetMapping("/home")
    public String mostrarHome(Model model)
    { 
        return "home";
    }


    @GetMapping("/login")
    public String mostrarLogin(Model model)
    { 
        return "login";
    }

    @PostMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
        return "redirect:/login";
    }

    @GetMapping("/panelAdmin")
    public String mostrarPanelAdmin(Model model, HttpSession session, RedirectAttributes redirectAttributes)
    {         
        if(sesionIsValid(session) == false || isAdmin(session)==false){
            return handleSesionInvalida(redirectAttributes);
        }

        return "admin/panelAdmin";
    }


}
