package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.EspecialidadEntity;
import com.linkfix.repository.EspecialidadRepository;
import com.linkfix.service.EspecialidadService;

@Service
public class EspecialidadServiceImpl implements EspecialidadService{

    @Autowired
    private EspecialidadRepository reposity;

    @Override
    public List<EspecialidadEntity> findAll() {
        return reposity.findAll();
    }

    @Override
    public List<EspecialidadEntity> findByIdTecnico(Long id) {
       
       return reposity.findByIdTecnico(id).orElse(null);
       
    }

}
