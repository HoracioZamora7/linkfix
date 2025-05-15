package com.linkfix.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkfix.entity.UsuarioRolEntity;
import com.linkfix.repository.UsuarioRolRepository;
import com.linkfix.service.UsuarioRolService;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Override
    public UsuarioRolEntity save(UsuarioRolEntity ur) {
        return usuarioRolRepository.save(ur);
    }

    @Override
    public List<UsuarioRolEntity> findAll() {
        return usuarioRolRepository.findAll();
    }
}
