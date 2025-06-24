package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkfix.entity.IncidenciaEntity;
import com.linkfix.repository.IncidenciaRepository;
import com.linkfix.service.IncidenciaService;

public class IncidenciaServiceImpl implements IncidenciaService {

    @Autowired
    private IncidenciaRepository reposity;
    
    @Override
    public List<IncidenciaEntity> findAll() {
        return reposity.findAll();
    }

}
