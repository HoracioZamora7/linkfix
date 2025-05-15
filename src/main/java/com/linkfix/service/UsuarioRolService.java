package com.linkfix.service;

import java.util.List;
import com.linkfix.entity.UsuarioRolEntity;

public interface UsuarioRolService {

    UsuarioRolEntity save(UsuarioRolEntity ur);

    List<UsuarioRolEntity> findAll();
}
