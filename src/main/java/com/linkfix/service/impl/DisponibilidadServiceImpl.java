package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkfix.entity.DisponibilidadEntity;
import com.linkfix.repository.DisponibilidadRepository;
import com.linkfix.service.DisponibilidadService;

public class DisponibilidadServiceImpl implements DisponibilidadService{

    @Autowired
    private DisponibilidadRepository reposity;

    @Override
    public List<DisponibilidadEntity> findAll() {
        return reposity.findAll();
    }

}
