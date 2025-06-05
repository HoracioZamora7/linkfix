package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkfix.entity.ElectrodomesticoEntity;
import com.linkfix.repository.ElectrodomesticoRepository;
import com.linkfix.service.ElectrodomesticoService;

public class ElectrodomesticoServiceImpl implements ElectrodomesticoService {

    @Autowired
    private ElectrodomesticoRepository reposity;

    
    @Override
    public List<ElectrodomesticoEntity> findAll() {
       return reposity.findAll();
    }

}
