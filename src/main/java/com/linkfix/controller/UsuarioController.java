package com.linkfix.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linkfix.dto.DniResponse;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Value("${api.token}")
    private String apiToken;

    private boolean isAdmin(HttpSession session) 
    {
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        return usuarioDTO != null && usuarioDTO.getRoles().contains(1); //si contiene el rol 1 (admin)
    }


    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario, Model model, RedirectAttributes redirectAttributes) {
        try {
            String dni = usuario.getPersona().getDni();

            // Validar formato DNI
             if (dni == null || dni.length() != 8 || !dni.matches("\\d+")) {
                redirectAttributes.addFlashAttribute("error", "El DNI ingresado no es válido.");
                return "redirect:/index";
            }

            //Llamar a la API externa para obtener los nombres
            String url = "https://apiperu.dev/api/dni/" + dni + "?api_token=" + apiToken;
            DniResponse response = restTemplate.getForObject(url, DniResponse.class);

            if (response == null || !response.isSuccess() || response.getData() == null)
            {
                redirectAttributes.addFlashAttribute("error", "No se encontraron nombres para el DNI ingresado.");
                return "redirect:/index";
            }

            // Cargar nombres y apellidos en el objeto Persona
            String nombre = response.getData().getNombre();
            String apellidos = response.getData().getApellidoPaterno() + " " + response.getData().getApellidoMaterno();
            usuario.getPersona().setNombre(nombre);
            usuario.getPersona().setApellidos(apellidos);
            
            logger.info("datos: " + nombre);
            logger.info("+datos: " + response.isSuccess());

            // Validar roles seleccionados
            if (!usuario.isCliente() && !usuario.isTecnico()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un rol.");
                return "redirect:/index";
            }

            personaService.save(usuario.getPersona());
            usuario.setEstado(estadoService.findById(1)); // Activo
            usuarioService.save(usuario);

            if (usuario.isCliente()) {
                UsuarioRolEntity clienterol = new UsuarioRolEntity();
                clienterol.setUsuario(usuario);
                clienterol.setRol(rolService.findById(2)); // Cliente
                usuarioRolSevice.save(clienterol);
            }

            if (usuario.isTecnico()) {
                UsuarioRolEntity tecnicorol = new UsuarioRolEntity();
                tecnicorol.setUsuario(usuario);
                tecnicorol.setRol(rolService.findById(3)); // Técnico
                usuarioRolSevice.save(tecnicorol);

                //api sunat aca
                usuario.setEstado(estadoService.findById(3)); //Pendiente
                usuarioService.update(usuario);

                //creacion de una solicitud de registro
                SolicitudRegistroEntity solicitudRegistro = new SolicitudRegistroEntity();
                solicitudRegistro.setTecnico(usuario);
                solicitudRegistroService.save(solicitudRegistro);

                redirectAttributes.addFlashAttribute("mensaje", "Registro enviado, pendiente de aprobación.");
                return "redirect:/index";
            }

            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado correctamente.");
            return "redirect:/index";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error durante el registro.");
            return "redirect:/index";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena,  HttpSession session,  Model model) 
    {
        //llevar gran parte de esto al service
        UsuarioEntity usuario = usuarioService.findByCorreo(correo);
    
        //si el usuario tiene el estado...
        switch (usuario.getEstado().getId()) {
            //activo
            case 1:
                if(passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                    UsuarioDTO usuarioDTO = UsuarioMapper.toSessionUsuarioDTO(usuario, usuarioRolSevice.findRolesByUsuario(usuario));
                    session.setAttribute("logueado", usuarioDTO);
                    return "redirect:/home";
                }
                else{
                    model.addAttribute("error", "Correo o contraseña incorrectos");
                }
                break;
            //inactivo
            case 2:
                model.addAttribute("error", "Cuenta desactivada."); //añadir un caso por cada estado...
                break;
            //pendiente
            case 3:
                model.addAttribute("error", "Cuenta pendiente de aprobación. Por favor inténtelo más tarde"); //añadir un caso por cada estado...
                break;
            default:
                break;
        }        

        return "login";
    }

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model, RedirectAttributes redirectAttributes) {     

        if (session.getAttribute("logueado") == null){
            redirectAttributes.addFlashAttribute("sesion inválida");
            return "redirect:/index";//sesion no valida
        }
        else{
            UsuarioDTO usuario = (UsuarioDTO) session.getAttribute("logueado");
            model.addAttribute("usuario", usuario);
        }

        return "/usuario/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(HttpSession session, Model model, RedirectAttributes redirectAttributes) {     

        if (session.getAttribute("logueado") == null){
            redirectAttributes.addFlashAttribute("sesion inválida");
            return "redirect:/index";//sesion no valida
        }
        else{
            UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
            model.addAttribute("usuario", usuarioDTO);
        }

        return "/usuario/perfil";
    }

    @PostMapping("/perfil/editar")
    public String guardarCambios(HttpSession session, @ModelAttribute("usuario") UsuarioDTO usuarioDTO, RedirectAttributes redirectAttributes)
    {

        try {
            if (session.getAttribute("logueado") == null){
                redirectAttributes.addFlashAttribute("sesion inválida");
                return "redirect:/index";//sesion no valida
            } 
            else{
                /* manejar logica de update */
            }
        
        } 
        catch (Exception e) {
            e.printStackTrace(); //cambiar por looger en el futuro 
            redirectAttributes.addFlashAttribute("Error inesperado");
            return "redirect:/index";//sesion no valida
        }
        

        return "redirect:/perfil";
    }

}
