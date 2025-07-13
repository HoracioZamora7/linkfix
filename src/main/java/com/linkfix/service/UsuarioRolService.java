package com.linkfix.service;

import java.util.List;

import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.entity.RolEntity;

public interface UsuarioRolService {

    UsuarioRolEntity save(UsuarioRolEntity ur);

    List<UsuarioRolEntity> findAll();
    List<RolEntity> findRolesByUsuario(UsuarioEntity u);
    List<RolEntity> findRolesbyIdUsuario(Long idUsuario);
}
