package com.linkfix.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.repository.UsuarioRolRepository;
import com.linkfix.service.UsuarioRolService;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

    @Autowired
    private UsuarioRolRepository repository;

    @Override
    public UsuarioRolEntity save(UsuarioRolEntity ur) {
        return repository.save(ur);
    }

    @Override
    public List<UsuarioRolEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<RolEntity> findRolesByUsuario(UsuarioEntity u) {
        
        return repository.findRolesByUsuario(u);
    }

    @Override
    public List<RolEntity> findRolesbyIdUsuario(Long idUsuario) {
        return repository.findRolesByIdUsuario(idUsuario);
    }
    
}
