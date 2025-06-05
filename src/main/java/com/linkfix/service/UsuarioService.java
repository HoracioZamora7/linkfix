package com.linkfix.service;

import java.util.List;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UsuarioEntity;

public interface UsuarioService {
    public List<UsuarioEntity> listAll();
    public UsuarioEntity save(UsuarioEntity u);
    public UsuarioEntity update(UsuarioEntity u);
    public Integer actualizarPerfil(UsuarioDTO usuarioDTO);
    public UsuarioEntity findByCorreo(String c);
    public UsuarioEntity findById(Long id);
    
    //mapeador
    public UsuarioDTO toSessionUsuarioDTO(UsuarioEntity usuario, List<RolEntity> roles);
}