package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.RolEntity;
import com.linkfix.repository.RolRepository;
import com.linkfix.service.RolService;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<RolEntity> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public RolEntity findById(int id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    public RolEntity findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
