package com.linkfix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.linkfix.entity.UsuarioEntity;
import com.linkfix.service.DepartamentoService;
import com.linkfix.service.UsuarioService;

@Controller
public class HomeController {

    /* @Autowired
    private UsuarioService service; */

    @Autowired
    private DepartamentoService deptService;

    @GetMapping("/registrar")
    public String mostrarRegistro(Model model)
    { 
        UsuarioEntity usuario = new UsuarioEntity();
        model.addAttribute("usuario", usuario);
        model.addAttribute("departamentos", deptService.findAll());
        return "registrar";
    }


}
