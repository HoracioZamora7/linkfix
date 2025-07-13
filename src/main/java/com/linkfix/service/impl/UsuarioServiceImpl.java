package com.linkfix.service.impl;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linkfix.controller.admin.AdminUsuarioController;
import com.linkfix.dto.ListadoUsuariosDTO;
import com.linkfix.dto.TecnicoListadoDTO;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.DepartamentoEntity;
import com.linkfix.entity.ProvinciaEntity;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UbigeoDistritosEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.aud.AUDUsuarioHistorial;
import com.linkfix.repository.UsuarioRepository;
import com.linkfix.repository.aud.AUDUsuarioHistorialRepository;
import com.linkfix.service.DepartamentoService;
import com.linkfix.service.EmailService;
import com.linkfix.service.PersonaService;
import com.linkfix.service.ProvinciaService;
import com.linkfix.service.UbigeoService;
import com.linkfix.service.UsuarioService;

import jakarta.mail.MessagingException;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UbigeoService ubigeoService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AUDUsuarioHistorialRepository audRepository;

    @Value("${app.url.base}")
    private String appBaseUrl;

    @Override
    public List<UsuarioEntity> listAll() 
    {
       return repository.findAll();
    }

    @Override
    public UsuarioEntity registrar(UsuarioEntity u) 
    {
        personaService.save(u.getPersona());
        u.setContrasena(passwordEncoder.encode(u.getContrasena()));
        u.setFecha_registro(LocalDateTime.now());
        u.setEmailToken(UUID.randomUUID().toString());
        u.setEmailTokenFechaExpiracion(LocalDateTime.now().plusMinutes(10));

        return generarToken(u);
    }

    @Override//
    public UsuarioEntity update(UsuarioEntity u) {
        /* u.setContrasena(passwordEncoder.encode(u.getContrasena())); */ //solo para update de contraseñas
        return repository.save(u);
    }

    @Override
    public UsuarioEntity findByCorreo(String c) {
        return repository.findByCorreo(c);
    }

    @Override
    public UsuarioEntity findById(Long id) {
        return repository.findById(id).orElse(null);
    }


    //integré el UsuarioMapper(clase static eliminada) al service
    public UsuarioDTO toSessionUsuarioDTO(UsuarioEntity usuario, List<RolEntity> roles) {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getPersona().getNombre());
        dto.setApellidos(usuario.getPersona().getApellidos());
        dto.setDni(usuario.getPersona().getDni());
        dto.setDireccion(usuario.getPersona().getDireccion());
        dto.setUbigeo(usuario.getPersona().getUbigeo().getId());
        dto.setCalificacion(usuario.getCalificacion());
        dto.setFecha_registro(usuario.getFecha_registro());
        dto.setTelefono(usuario.getPersona().getTelefono());
        //ruc
        dto.setRuc(usuario.getPersona().getRuc());

        //temporal
        DepartamentoEntity departamento = departamentoService.findById(usuario.getPersona().getUbigeo().getDepartmentId()).orElse(null);
        ProvinciaEntity provincia = provinciaService.findById(usuario.getPersona().getUbigeo().getProvinceId()).orElse(null);
        dto.setDepartamento(departamento != null ? departamento.getName() : null);
        dto.setProvincia(provincia != null ? provincia.getName() : null);
        dto.setDistrito(usuario.getPersona().getUbigeo().getName());
        dto.setDepartamentoId(dto.getUbigeo().substring(0, 2));
        dto.setProvinciaId(dto.getUbigeo().substring(0, 4));
        //
        
        List<Integer> rolesId = roles.stream()
                                    .map(RolEntity::getId) 
                                    .collect(Collectors.toList());
        dto.setRoles(rolesId);

        List<String> rolesNombre = roles.stream()
                                        .map(RolEntity::getNombre)
                                        .collect(Collectors.toList());
        dto.setNombresRoles(rolesNombre);

        return dto;
    }

    private UsuarioEntity usuarioDTOToUsuarioEntity(UsuarioDTO dto)
    {
        UsuarioEntity entity = findById(dto.getId());
        entity.setCorreo(dto.getCorreo());
        
        entity.getPersona().setTelefono(dto.getTelefono());
        entity.getPersona().setDireccion(dto.getDireccion());

        UbigeoDistritosEntity ubigeoEntity = ubigeoService.findById(dto.getUbigeo()).orElse(null);
        entity.getPersona().setUbigeo(ubigeoEntity);

        return entity;
    }

    @Override
    public boolean actualizarPerfil(UsuarioDTO usuarioDTO, Long idUsuarioUltimaEdicion) {
        try {
            UsuarioEntity usuarioEntity = usuarioDTOToUsuarioEntity(usuarioDTO);
            usuarioEntity.setIdUsuarioUltimaEdicion(idUsuarioUltimaEdicion);
            
            personaService.save(usuarioEntity.getPersona());
            repository.save(usuarioEntity);

            // Crear historial
            AUDUsuarioHistorial historial = new AUDUsuarioHistorial();
            historial.setIdUsuario(usuarioEntity.getId());
            historial.setCorreo(usuarioEntity.getCorreo());
            historial.setIdEstado(usuarioEntity.getEstado().getId());
            historial.setNombreEstado(usuarioEntity.getEstado().getNombre());
            historial.setFechaRegistro(usuarioEntity.getFecha_registro());
            historial.setEmailToken(usuarioEntity.getEmailToken());
            historial.setEmailTokenFechaExpiracion(usuarioEntity.getEmailTokenFechaExpiracion());
            historial.setIdUsuarioUltimaEdicion(idUsuarioUltimaEdicion);
            UsuarioEntity usuarioEntity2=repository.findById(idUsuarioUltimaEdicion).orElse(null);
            historial.setCorreoUsuarioUltimaEdicion(usuarioEntity2.getCorreo());
            historial.setIdPersona(usuarioEntity.getPersona().getCodigo());
            historial.setNombre(usuarioEntity.getPersona().getNombre());
            historial.setApellidos(usuarioEntity.getPersona().getApellidos());
            historial.setDni(usuarioEntity.getPersona().getDni());
            historial.setRuc(usuarioEntity.getPersona().getRuc());
            historial.setIdUbigeo(usuarioEntity.getPersona().getUbigeo().getId());
            historial.setTelefono(usuarioEntity.getPersona().getTelefono());
            historial.setDireccion(usuarioEntity.getPersona().getDireccion());
            historial.setFechaCambio(LocalDateTime.now());
            audRepository.save(historial);



            return true;

        } catch (Exception e) {
            return false;
        }
    }

    ///temporal
    @Override
    public boolean actualizarUsuario(UsuarioEntity usuarioEntity, Long idUsuarioUltimaEdicion) {
        try {
            usuarioEntity.setIdUsuarioUltimaEdicion(idUsuarioUltimaEdicion);
            
            personaService.save(usuarioEntity.getPersona());
            repository.save(usuarioEntity);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UsuarioEntity findByEmailToken(String token) {
        return repository.findByEmailToken(token);
    }

    @Override
    public UsuarioEntity generarToken(UsuarioEntity u) {
        u.setEmailToken(UUID.randomUUID().toString());
        u.setEmailTokenFechaExpiracion(LocalDateTime.now().plusMinutes(10));
        //email sender sencillo
        //emailService.sendEmail(u.getCorreo(), "Token", "http://localhost:8080/registrar/verificar-email?token="+ u.getEmailToken());

        Map<String, String> variables = new HashMap<>();
        variables.put("username", u.getPersona().getApellidos()+", " + u.getPersona().getNombre());
        variables.put("enlaceConfirmacion", appBaseUrl+"/registrar/verificar-email?token="+u.getEmailToken());

        try {
            emailService.sendHtmlEmail(u.getCorreo(), "Verificación de cuenta", "templates/email/templateMailConfirmacion.html", variables);
        } catch (MessagingException e) {
            emailService.sendEmail(u.getCorreo(), "Token", appBaseUrl+"/registrar/verificar-email?token="+ u.getEmailToken());
            e.printStackTrace();
        }
        
        return repository.save(u);
    }

    @Override
    public Page<TecnicoListadoDTO> listarTecnicosDisponibles(String idUbigeo, Long idElectrodomestico, Integer idDia,
            LocalTime horaInicio, LocalTime horaFin, Pageable pageable) {
        
        return repository.listarTecnicosDisponibles(idUbigeo, idElectrodomestico, idDia, horaInicio, horaFin, pageable);
    }

    @Override
    public Page<ListadoUsuariosDTO> listarUsuarios(String correo, Pageable pageable) {

        
        return repository.listarUsuarios(correo, pageable);
    }

    @Override
    public UsuarioDTO toUsuarioDTOById(Long id) {
        UsuarioEntity usuario = repository.findById(id).orElse(null);
        UsuarioDTO dto = new UsuarioDTO();
        
        if(usuario!=null)
        {
        dto.setId(usuario.getId());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getPersona().getNombre());
        dto.setApellidos(usuario.getPersona().getApellidos());
        dto.setDni(usuario.getPersona().getDni());
        dto.setDireccion(usuario.getPersona().getDireccion());
        dto.setUbigeo(usuario.getPersona().getUbigeo().getId());
        dto.setCalificacion(usuario.getCalificacion());
        dto.setFecha_registro(usuario.getFecha_registro());
        dto.setTelefono(usuario.getPersona().getTelefono());
        dto.setRuc(usuario.getPersona().getRuc());
        DepartamentoEntity departamento = departamentoService.findById(usuario.getPersona().getUbigeo().getDepartmentId()).orElse(null);
        ProvinciaEntity provincia = provinciaService.findById(usuario.getPersona().getUbigeo().getProvinceId()).orElse(null);
        dto.setDepartamento(departamento != null ? departamento.getName() : null);
        dto.setProvincia(provincia != null ? provincia.getName() : null);
        dto.setDistrito(usuario.getPersona().getUbigeo().getName());
        dto.setDepartamentoId(dto.getUbigeo().substring(0, 2));
        dto.setProvinciaId(dto.getUbigeo().substring(0, 4));
        }
        
        return dto;
    }
private static final Logger logger = LoggerFactory.getLogger(AdminUsuarioController.class);
    @Override
    public void recalcularCalificacion(Long idTecnico) {
        UsuarioEntity usuarioEntity = repository.findById(idTecnico).orElse(null);
        Float nuevaCalificación=repository.recalcularCalificacion(idTecnico);
        
        usuarioEntity.setCalificacion(nuevaCalificación);

        repository.save(usuarioEntity);
        logger.info("Desde usuarioService:" + nuevaCalificación.toString());
        return;
    }

    

}
