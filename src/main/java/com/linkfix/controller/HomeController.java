package com.linkfix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linkfix.entity.UsuarioEntity;
import com.linkfix.service.DepartamentoService;
import com.linkfix.service.SolicitudRegistroService;

@Controller
public class HomeController {

    /* @Autowired
    private UsuarioService service; */

    @Autowired
    private DepartamentoService deptService;

    @Autowired
    private SolicitudRegistroService solicitudService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model)
    { 
        UsuarioEntity usuario = new UsuarioEntity();
        model.addAttribute("usuario", usuario);
        model.addAttribute("departamentos", deptService.findAll());
        return "registro";
    }

    @GetMapping("/index")
    public String mostrarIndex(@RequestParam(value = "error", required = false) String error, Model model)
    {
        if (error != null) {
            String tipoMensaje = null, mensaje = null;

            switch (error) {
                case "1":
                    tipoMensaje = "error";
                    mensaje = "Datos inválidos al intentar crear cuenta";
                    break;
                case "2":
                    tipoMensaje = "error";
                    mensaje = "Debes seleccionar, por lo menos, un rol!";
                    break;
                case "3":
                    tipoMensaje = "aviso";
                    mensaje = "Cuenta pendiente de aprobación!";
                    break;
                case "4":
                    tipoMensaje = "error";
                    mensaje = "Sesión inválida";
                    break;
                default:
                    tipoMensaje = "error";
                    mensaje = "Hubo un error";
                    break;
            }
            
            model.addAttribute(tipoMensaje, mensaje);
        }

        return "index";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model)
    { 
        return "login";
    }

    /* @GetMapping("validarTecnicos")
    public String mostrarValidarTecnicos(Model model)
    {
        model.addAttribute("listaSolicitudes", solicitudService.findAll());
        return "validarTecnicos";
    } */

}
