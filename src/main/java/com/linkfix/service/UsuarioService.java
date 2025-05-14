package com.linkfix.service;

import java.util.List;

import com.linkfix.entity.UsuarioEntity;

public interface UsuarioService {
    public List<UsuarioEntity> listAll();
    public UsuarioEntity save(UsuarioEntity u);
}