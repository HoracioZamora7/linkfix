package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.linkfix.entity.DiaEntity;
import com.linkfix.repository.DiaRepository;
import com.linkfix.service.DiaService;

public class DiaServiceImpl implements DiaService {

    @Autowired
    private DiaRepository reposity;

    @Override
    public List<DiaEntity> findAll() {
        return reposity.findAll();
    }

}
