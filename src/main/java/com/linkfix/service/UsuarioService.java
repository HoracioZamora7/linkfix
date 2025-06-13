package com.linkfix.service;

import java.util.List;

import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UsuarioEntity;

public interface UsuarioService {
    public List<UsuarioEntity> listAll();
    public UsuarioEntity registrar(UsuarioEntity u);
    public UsuarioEntity update(UsuarioEntity u);
    public Integer actualizarPerfil(UsuarioDTO usuarioDTO);
    public UsuarioEntity findByCorreo(String c);
    public UsuarioEntity findById(Long id);
    public UsuarioEntity findByEmailToken(String token);
    public UsuarioEntity generarToken(UsuarioEntity u);
    
    //mapeador
    public UsuarioDTO toSessionUsuarioDTO(UsuarioEntity usuario, List<RolEntity> roles);
}