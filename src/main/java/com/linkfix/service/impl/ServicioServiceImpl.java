package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.ServicioEntity;
import com.linkfix.repository.ServicioRepository;
import com.linkfix.service.ServicioService;

@Service
public class ServicioServiceImpl implements ServicioService{

    @Autowired
    private ServicioRepository reposity;

    @Override
    public List<ServicioEntity> findAll() {
        return reposity.findAll();
    }

}
