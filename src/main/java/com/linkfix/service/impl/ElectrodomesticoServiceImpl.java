package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.ElectrodomesticoEntity;
import com.linkfix.repository.ElectrodomesticoRepository;
import com.linkfix.service.ElectrodomesticoService;

@Service
public class ElectrodomesticoServiceImpl implements ElectrodomesticoService {

    @Autowired
    private ElectrodomesticoRepository reposity;

    
    @Override
    public List<ElectrodomesticoEntity> findAll() {
       return reposity.findAll();
    }


    @Override
    public ElectrodomesticoEntity findById(Long id) {
        return reposity.findById(id).orElse(null);
    }

}
