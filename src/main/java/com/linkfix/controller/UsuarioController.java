package com.linkfix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.linkfix.entity.UsuarioEntity;
import com.linkfix.service.UsuarioService;


@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario)
    {
        usuarioService.save(usuario);
        //falta logica de wardar persona 
        return "redirect:/home";
    }
}
