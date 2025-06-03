package com.linkfix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.SolicitudRegistroEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.mapper.UsuarioMapper;
import com.linkfix.service.EstadoService;
import com.linkfix.service.PersonaService;
import com.linkfix.service.RolService;
import com.linkfix.service.SolicitudRegistroService;
import com.linkfix.service.UsuarioRolService;
import com.linkfix.service.UsuarioService;

import jakarta.servlet.http.HttpSession;


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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private SolicitudRegistroService solicitudRegistroService;

    private boolean isAdmin(HttpSession session) 
    {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        return usuarioDTO != null && usuarioDTO.getRoles().contains(1); //si contiene el rol 1 (admin)
    }


    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario)
    {
        try {
            
            if(usuario.isCliente()==false && usuario.isTecnico()==false)
            {
                return "redirect:/index?error=2";
            }                           
            personaService.save(usuario.getPersona());
            usuario.setEstado(estadoService.findById(1)); //activo
            usuarioService.save(usuario);
            
            //System.out.println("Cliente: " + usuario.isCliente() + ", Técnico: " + usuario.isTecnico());
            if(usuario.isCliente())
            {
                UsuarioRolEntity clienterol = new UsuarioRolEntity(); 
                clienterol.setUsuario(usuario);
                clienterol.setRol(rolService.findById(2)); //rol cliente
                usuarioRolSevice.save(clienterol);
            }

            if(usuario.isTecnico())
            {
                UsuarioRolEntity clienterol = new UsuarioRolEntity();
                clienterol.setUsuario(usuario);
                clienterol.setRol(rolService.findById(3)); //rol tecnico
                usuarioRolSevice.save(clienterol);

                usuario.setEstado(estadoService.findById(3)); //pendiente
                usuarioService.update(usuario);

                SolicitudRegistroEntity solicitudRegistro = new SolicitudRegistroEntity();

                //crear solicitud de registro
                solicitudRegistro.setTecnico(usuario);
                solicitudRegistroService.save(solicitudRegistro);
                return "redirect:/index?error=3";
            }
        

        return "redirect:/index";

        } catch (Exception e) {
            e.printStackTrace(); //cambiar por looger en el futuro 
            return "redirect:/index?error=1";
        }
       
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo,  @RequestParam("contrasena") String contrasena,  HttpSession session,  Model model) 
    {
        //llevar gran parte de esto al service
        UsuarioEntity usuario = usuarioService.findByCorreo(correo);
    
        //si el usuario tiene el estado "Activo"...
        if(usuario.getEstado().getId() == 1) {
            if (usuario != null && passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                UsuarioDTO usuarioDTO = UsuarioMapper.toSessionUsuarioDTO(usuario, usuarioRolSevice.findRolesByUsuario(usuario));
                session.setAttribute("logueado", usuarioDTO);  //que se pone en el atributo string?
                return "redirect:/home";
            }
            else{
                model.addAttribute("error", "Correo o contraseña incorrectos");
            }
        }
        else{
            model.addAttribute("error", "Cuenta pendiente de aprobación. Por favor inténtelo más tarde"); //añadir un caso por cada estado...
        }

        return "login";
        
    }





    ///zamora 
    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {     

        if (session.getAttribute("logueado") == null){
            return "redirect:/index?error=4";//sesion no valida
        }
        else{
            UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("logueado");
            model.addAttribute("usuario", usuario);
        }

        return "/usuario/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(HttpSession session, Model model) {     

        if (session.getAttribute("logueado") == null){
            return "redirect:/index?error=4";//sesion no valida
        }
        else{
            UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
            model.addAttribute("usuario", usuarioDTO);
        }

        return "/usuario/perfil";
    }

    @PostMapping("/perfil/editar")
    public String guardarCambios(HttpSession session, @ModelAttribute("usuario") UsuarioDTO usuarioDTO)
    {

        try {
            if (session.getAttribute("logueado") == null){
                return "redirect:/index?error=4";//sesion no valida
            } 
            else{
                
            }
        
        } 
        catch (Exception e) {
            e.printStackTrace(); //cambiar por looger en el futuro 
            return "redirect:/index?error=1";
        }
        
        
        return "redirect:/perfil";
    }

}
