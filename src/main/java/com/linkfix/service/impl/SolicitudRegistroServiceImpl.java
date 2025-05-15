package com.linkfix.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkfix.entity.SolicitudRegistroEntity;
import com.linkfix.repository.SolicitudRegistroRepository;
import com.linkfix.service.SolicitudRegistroService;

@Service
public class SolicitudRegistroServiceImpl implements SolicitudRegistroService {

    @Autowired
    private SolicitudRegistroRepository repository;

    @Override
    public SolicitudRegistroEntity save(SolicitudRegistroEntity s) {
        s.setEstado(false);
        s.setFecha_registro(LocalDateTime.now());
        return repository.save(s);
    }

    @Override
    public List<SolicitudRegistroEntity> findAll() {
        return repository.findAll(); //crear dto para este?
    }

    @Override
    public SolicitudRegistroEntity findById(Long id) {
        
        return repository.findById(id).orElse(null); //crear dto para este
    }

    @Override
    public SolicitudRegistroEntity update(SolicitudRegistroEntity s) {
        s.setFecha_revision(LocalDateTime.now());
        s.setEstado(true);
        return repository.save(s);
    }

    @Override
    public List<SolicitudRegistroEntity> findAllByEstado(boolean estado) {
        return repository.findAllByEstado(estado);
    }
}
