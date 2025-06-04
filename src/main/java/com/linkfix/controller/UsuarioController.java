package com.linkfix.controller;

import com.linkfix.dto.DniResponse;
import com.linkfix.entity.SolicitudRegistroEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.mapper.UsuarioMapper;
import com.linkfix.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Value("${api.token}")
    private String apiToken;

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioEntity usuario, Model model) {
        try {
            String dni = usuario.getPersona().getDni();

            // Validar formato DNI
         /*    if (dni == null || dni.length() != 8 || !dni.matches("\\d+")) {
                model.addAttribute("error", "El DNI ingresado no es válido.");
                return "registro"; // Asegúrate que este es el nombre del archivo .html del formulario
            }
 */
            // Llamar a la API externa para obtener los nombres
            String url = "https://apiperu.dev/api/dni/" + dni + "?api_token=" + apiToken;
            DniResponse response = restTemplate.getForObject(url, DniResponse.class);

            if (response == null || !response.isSuccess() || response.getData() == null) {
                model.addAttribute("error", "No se encontraron nombres para el DNI ingresado.");
                return "index";
            }

            // Cargar nombres y apellidos en el objeto Persona
        String nombre = response.getData().getNombre();
        String apellidos = response.getData().getApellidos();
        System.out.println("datos: " + response.getData().getNombre());

        usuario.getPersona().setNombre(nombre);
        usuario.getPersona().setApellidos(apellidos);
            // Validar roles seleccionados
            if (!usuario.isCliente() && !usuario.isTecnico()) {
                model.addAttribute("error", "Debe seleccionar al menos un rol.");
                return "index";
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

                usuario.setEstado(estadoService.findById(3)); // Pendiente
                usuarioService.update(usuario);

                SolicitudRegistroEntity solicitudRegistro = new SolicitudRegistroEntity();
                solicitudRegistro.setTecnico(usuario);
                solicitudRegistroService.save(solicitudRegistro);

                model.addAttribute("error", "Registro enviado, pendiente de aprobación.");
                return "index";
            }

            model.addAttribute("error", "Usuario registrado correctamente.");
            return "index";

        } catch (Exception e) {
            e.printStackTrace(); // Mejor reemplazar por Logger
            model.addAttribute("error", "Ocurrió un error durante el registro.");
            return "index";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo,
                        @RequestParam("contrasena") String contrasena,
                        HttpSession session,
                        Model model) {

        UsuarioEntity usuario = usuarioService.findByCorreo(correo);

        if (usuario != null && usuario.getEstado().getId() == 1) {
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                session.setAttribute("logueado", UsuarioMapper.toSessionUsuarioDTO(usuario, usuarioRolSevice.findRolesByUsuario(usuario)));
                return "home";
            } else {
                model.addAttribute("error", "Correo o contraseña incorrectos.");
            }
        } else {
            model.addAttribute("error", "Cuenta pendiente de aprobación o no activa.");
        }

        return "login";
    }
}
