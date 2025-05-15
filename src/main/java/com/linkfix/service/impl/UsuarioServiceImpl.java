package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linkfix.entity.UsuarioEntity;
import com.linkfix.repository.UsuarioRepository;
import com.linkfix.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioEntity> listAll() 
    {
       return repository.findAll();
    }

    @Override
    public UsuarioEntity save(UsuarioEntity u) 
    {
        u.setContrasena(passwordEncoder.encode(u.getContrasena()));
        return repository.save(u);
    }

    @Override
    public UsuarioEntity update(UsuarioEntity u) {
        u.setContrasena(passwordEncoder.encode(u.getContrasena()));
        return repository.save(u);
    }
}
