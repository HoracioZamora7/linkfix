package com.linkfix.mapper;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.RolEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static UsuarioDTO toSessionUsuarioDTO(UsuarioEntity usuario, List<RolEntity> roles) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getPersona().getNombre());
        dto.setApellidos(usuario.getPersona().getApellidos());
        
        List<Integer> rolesId = roles.stream()
                                    .map(RolEntity::getId)
                                    .collect(Collectors.toList());
        dto.setRoles(rolesId);

        return dto;
    }
}
