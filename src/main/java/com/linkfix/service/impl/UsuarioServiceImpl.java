package com.linkfix.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.DepartamentoEntity;
import com.linkfix.entity.ProvinciaEntity;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UbigeoDistritosEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.repository.UsuarioRepository;
import com.linkfix.service.DepartamentoService;
import com.linkfix.service.EmailService;
import com.linkfix.service.PersonaService;
import com.linkfix.service.ProvinciaService;
import com.linkfix.service.UbigeoService;
import com.linkfix.service.UsuarioService;

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
        u.setEmailTokenFechaExpiracion(LocalDateTime.now().plusMinutes(1));

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
    public Integer actualizarPerfil(UsuarioDTO usuarioDTO) {
        try {
            UsuarioEntity usuarioEntity = usuarioDTOToUsuarioEntity(usuarioDTO);
            personaService.save(usuarioEntity.getPersona());
            repository.save(usuarioEntity);

            return 1;
            



        } catch (Exception e) {
            return 2;
        }
    }

    @Override
    public UsuarioEntity findByEmailToken(String token) {
        return repository.findByEmailToken(token);
    }

    @Override
    public UsuarioEntity generarToken(UsuarioEntity u) {
        u.setEmailToken(UUID.randomUUID().toString());
        u.setEmailTokenFechaExpiracion(LocalDateTime.now().plusMinutes(1));
        emailService.sendEmail(u.getCorreo(), "Token", "http://localhost:8080/registrar/verificar-email?token="+ u.getEmailToken());

        
        return repository.save(u);
    }
}
