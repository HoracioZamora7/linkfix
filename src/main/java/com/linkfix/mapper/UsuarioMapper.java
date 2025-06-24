package com.linkfix.mapper;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.service.DepartamentoService;
import com.linkfix.service.ProvinciaService;
import com.linkfix.entity.DepartamentoEntity;
import com.linkfix.entity.ProvinciaEntity;
import com.linkfix.entity.RolEntity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

public class UsuarioMapper {
/* 
    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

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

        //temporal
        DepartamentoEntity departamento = departamentoService.findById(usuario.getPersona().getUbigeo().getDepartmentId()).orElse(null);
        ProvinciaEntity provincia = provinciaService.findById(usuario.getPersona().getUbigeo().getProvinceId()).orElse(null);
        dto.setDepartamento(departamento.getName());
        dto.setProvincia(provincia.getName());
        dto.setDistrito(usuario.getPersona().getUbigeo().getName());
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
    } */
}
