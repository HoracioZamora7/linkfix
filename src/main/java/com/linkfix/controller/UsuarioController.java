package com.linkfix.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.linkfix.dto.DniResponse;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.DisponibilidadEntity;
import com.linkfix.entity.SolicitudRegistroEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.entity.aud.AUDUsuarioHistorial;
import com.linkfix.service.DepartamentoService;
import com.linkfix.service.DiaService;
import com.linkfix.service.DisponibilidadService;
import com.linkfix.service.EstadoService;
import com.linkfix.service.RolService;
import com.linkfix.service.SolicitudRegistroService;
import com.linkfix.service.UsuarioRolService;
import com.linkfix.service.UsuarioService;
import com.linkfix.service.aud.AUDUsuarioHistorialService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static com.linkfix.util.SesionUtils.*;


@Controller
public class UsuarioController {

    @Autowired
    private AUDUsuarioHistorialService audUsuarioHistorialService;

    @Autowired
    private UsuarioService usuarioService;

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
    private DepartamentoService deptService;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private DiaService diaService;

    @Value("${api.token}")
    private String apiToken;

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario, Model model, RedirectAttributes redirectAttributes) {
        try {
            UsuarioEntity usuarioExistente = usuarioService.findByCorreo(usuario.getCorreo());
            //si ya existe el correo..
            if(usuarioExistente!=null)
            {
                //pero si tiene el estado 5 (correo sin confirmar)
                if(usuarioExistente.getEstado().getId()==5)
                {
                    usuarioExistente.setCorreo(usuario.getCorreo());  //se actualiza unicamente el correo
                    usuarioService.generarToken(usuarioExistente); //se le genera un nuevo token
                    redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado. Por favor, revise la bandeja de entrada de su correo. Tienes un minuto para verificar el correo");
                }
                else{
                    redirectAttributes.addFlashAttribute("error", "Correo ya existente.");
                }

                return "redirect:/index";
            }
            
            // Validar roles seleccionados
            if (!usuario.isCliente() && !usuario.isTecnico()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un rol.");
                return "redirect:/index";
            }
            String dni = usuario.getPersona().getDni();
            // Validar formato DNI
            if (dni == null || dni.length() != 8 || !dni.matches("\\d+")) {
                redirectAttributes.addFlashAttribute("error", "El DNI ingresado no es válido.");
                return "redirect:/index";
            }

            //Llamar a la API externa para obtener los nombres
            String url = "https://apiperu.dev/api/dni/" + dni + "?api_token=" + apiToken;
            DniResponse response = restTemplate.getForObject(url, DniResponse.class);
            //validar que el dni exista
            if (response == null || !response.isSuccess() || response.getData() == null)
            {
                redirectAttributes.addFlashAttribute("error", "No se encontraron nombres para el DNI ingresado.");
                return "redirect:/index";
            }

            // Cargar nombres y apellidos en el objeto Persona
            usuario.getPersona().setNombre(response.getData().getNombre());
            usuario.getPersona().setApellidos(response.getData().getApellidoPaterno() + " " + response.getData().getApellidoMaterno());
            
            //persistir datos
            
            usuario.setEstado(estadoService.findById(5)); //Correo no verificado
            usuarioService.registrar(usuario);
            
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
                //usuario.setEstado(estadoService.findById(3)); //Pendiente
                //usuarioService.update(usuario);
                //creacion de una solicitud de registro
                SolicitudRegistroEntity solicitudRegistro = new SolicitudRegistroEntity();
                solicitudRegistro.setTecnico(usuario);
                solicitudRegistroService.save(solicitudRegistro);
                //redirectAttributes.addFlashAttribute("mensaje", "Registro enviado, pendiente de aprobación.");
                //return "redirect:/index";
            }


            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado. Por favor, revise la bandeja de entrada de su correo. Tienes diez minutos para verificar el correo");
            return "redirect:/index";

        } catch (Exception e) {
            e.printStackTrace(); //aca se debería eliminar el registro de persona y usuario
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error durante el registro.");
            return "redirect:/index";
        }
    }

    @GetMapping("/registrar/verificar-email")
    public String verificarMail(@RequestParam("token") String emailToken, RedirectAttributes redirectAttributes)
    {
        UsuarioEntity usuarioEntity = usuarioService.findByEmailToken(emailToken);

        if(usuarioEntity == null || usuarioEntity.getEstado().getId()!=5)
        {
            redirectAttributes.addFlashAttribute("error", "Token inválido");
            return "redirect:/index";
        }

        if(usuarioEntity.getEmailTokenFechaExpiracion().isBefore(LocalDateTime.now()))
        {
            redirectAttributes.addFlashAttribute("error", "Token expirado. Se generará uno nuevo.");
            usuarioService.generarToken(usuarioEntity);
            return "redirect:/index";
        }

        if(usuarioEntity.getEmailToken().equals(emailToken))
        {
            if(usuarioRolSevice.findRolesByUsuario(usuarioEntity).contains(rolService.findById(3)))//si es tecnico
            {
                usuarioEntity.setEstado(estadoService.findById(3)); //Pendiente
                
                redirectAttributes.addFlashAttribute("mensaje", "Cuenta activada. Usuario tipo técnico pendiente de aprobación");
            }
            else{
                redirectAttributes.addFlashAttribute("mensaje", "Cuenta activada. Por favor, inicie sesión");
                usuarioEntity.setEstado(estadoService.findById(1)); //Activo
            }

            usuarioService.update(usuarioEntity);
            return "redirect:/login";
        }
        
        redirectAttributes.addFlashAttribute("error", "Error en la validacion");
        return "redirect:/index";
        
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena,  HttpSession session,  Model model) 
    {
        //llevar gran parte de esto al service
        UsuarioEntity usuario = usuarioService.findByCorreo(correo);
        if(usuario==null){
            model.addAttribute("error", "Correo no registrado.");
            return "login";
        }
    
        if (usuario.getEstado().getId()==1)
        {
            if(passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                UsuarioDTO usuarioDTO = usuarioService.toSessionUsuarioDTO(usuario, usuarioRolSevice.findRolesByUsuario(usuario));
                session.setAttribute("logueado", usuarioDTO);//variable sesion
                return "redirect:/home";
            }
            else{
                model.addAttribute("error", "Contraseña incorrectos");
            }
        }
        else{
            model.addAttribute("error", "No puedes logearte aún, tienes el estado: "+usuario.getEstado().getNombre());
        }

        return "login";
    }

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model, RedirectAttributes redirectAttributes) {     

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }
        else{
            UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
            //consultar disponibilidad (lista)
            List<DisponibilidadEntity> disponibilidad = disponibilidadService.findByIdTecnico(usuarioDTO.getId()).orElse(null);
            
            model.addAttribute("usuarioDTO", usuarioDTO);
            model.addAttribute("disponibilidad", disponibilidad);
        }

        return "/usuario/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(HttpSession session, Model model, RedirectAttributes redirectAttributes) {     

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }
        
        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        model.addAttribute("usuarioDTO", usuarioDTO);
        model.addAttribute("departamentos", deptService.findAll());
        return "/usuario/editar";
    }

    @PostMapping("/perfil/editar")
    public String guardarCambiosPerfil(HttpSession session, @ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, RedirectAttributes redirectAttributes, Model model)
    {

        try {
            if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
            }

            //mejorar esto
            UsuarioDTO sessionUsuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
            usuarioDTO.setId(sessionUsuarioDTO.getId());
            usuarioService.actualizarPerfil(usuarioDTO, sessionUsuarioDTO.getId());

            /* if(sessionUsuarioDTO.getCorreo()!=usuarioDTO.getCorreo())
            {
                session.invalidate();
                redirectAttributes.addFlashAttribute("mensaje", "Correo actualizado. Inicie sesión nuevamente");
                return "redirect:/index";
            } */
            
        } 
        catch (Exception e) {
            e.printStackTrace(); //cambiar por looger en el futuro 
            redirectAttributes.addFlashAttribute("error","Error inesperado");
            return "redirect:/index";//sesion no valida
        }
        
        model.addAttribute("usuarioDTO", usuarioDTO);
        model.addAttribute("departamentos", deptService.findAll());
        return "/usuario/perfil";
    }


    
    @GetMapping("/perfil/disponibilidad")
    public String mostrarFormularioDisponibilidad(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        List<DisponibilidadEntity> disponibilidad = disponibilidadService.findByIdTecnico(usuarioDTO.getId()).orElse(null);
        model.addAttribute("disponibilidad", disponibilidad);
        model.addAttribute("dias", diaService.findAll());

        return "/usuario/disponibilidad";
    }
    


    @PostMapping("/perfil/disponibilidad")
    public String guardarDisponibilidad(@RequestParam Integer idDia, @RequestParam String formHoraInicio, @RequestParam String formHoraFin, HttpSession session, Model model, RedirectAttributes redirectAttributes) 
    {

        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }

        LocalTime horaInicio = LocalTime.parse(formHoraInicio);
        LocalTime horaFin = LocalTime.parse(formHoraFin);

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        
        switch (disponibilidadService.save(horaInicio, horaFin, idDia, usuarioDTO.getId())) {
            case 1:
                //incompatible
                model.addAttribute("mensaje", "Existe incompatibilidad horariaa");
                break;
        
            default:
                break;
        }


        List<DisponibilidadEntity> disponibilidad = disponibilidadService.findByIdTecnico(usuarioDTO.getId()).orElse(null);
        model.addAttribute("disponibilidad", disponibilidad);
        model.addAttribute("dias", diaService.findAll());

        return "/usuario/disponibilidad";
    }


    @PostMapping("/perfil/disponibilidad/eliminar")
    public String eliminarDisponibilidad(HttpSession session, @RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) 
    {
        if (!sesionIsValid(session)) {
            return handleSesionInvalida(redirectAttributes);
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) session.getAttribute("logueado");
        
        if(!disponibilidadService.deleteById(id, usuarioDTO.getId())){
            redirectAttributes.addFlashAttribute("error", "eliminación fallida");
            return "redirect:/index";
        }


        model.addAttribute("disponibilidad", disponibilidadService.findByIdTecnico(usuarioDTO.getId()).orElse(null));
        model.addAttribute("dias", diaService.findAll());

        return "/usuario/disponibilidad";
    }



}
