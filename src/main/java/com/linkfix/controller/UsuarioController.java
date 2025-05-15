package com.linkfix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.linkfix.entity.EstadoEntity;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.service.EstadoService;
import com.linkfix.service.PersonaService;
import com.linkfix.service.RolService;
import com.linkfix.service.UsuarioRolService;
import com.linkfix.service.UsuarioService;

import jakarta.persistence.Transient;


@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private UsuarioRolService usuarioRolSevice;

    @Autowired
    private RolService rolService;

    @Autowired
    private EstadoService estadoService;


    @Transient
    private List<String> rolesSeleccionados;

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario)
    {
        personaService.save(usuario.getPersona());
        usuario.setEstado(estadoService.findByNombre("Activo"));
        usuarioService.save(usuario);
        
        UsuarioRolEntity clienterol = new UsuarioRolEntity();
        clienterol.setUsuario(usuario);
        
        if(usuario.isCliente())
        {
            clienterol.setRol(rolService.findByNombre("Cliente"));
            usuarioRolSevice.save(clienterol);
        }
        
        if(usuario.isTecnico())
        {
            clienterol.setRol(rolService.findByNombre("Técnico"));
            usuarioRolSevice.save(clienterol);
            usuarioService.update(usuario);
        }

    
        return "redirect:/index";
    }


    
}
