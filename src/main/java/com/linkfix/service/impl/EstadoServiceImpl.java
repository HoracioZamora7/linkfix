package com.linkfix.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.repository.EstadoRepository;
import com.linkfix.entity.EstadoEntity;
import com.linkfix.service.EstadoService;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoServiceImpl implements EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public List<EstadoEntity> findAll() {
        return estadoRepository.findAll();
    }

    @Override
    public EstadoEntity findById(int id) {
        Optional<EstadoEntity> optional = estadoRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public EstadoEntity findByNombre(String nombre) {
        return estadoRepository.findByNombre(nombre);
    }
}
