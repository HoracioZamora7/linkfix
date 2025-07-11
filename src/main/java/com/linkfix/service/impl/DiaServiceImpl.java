package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.DiaEntity;
import com.linkfix.repository.DiaRepository;
import com.linkfix.service.DiaService;

@Service
public class DiaServiceImpl implements DiaService {

    @Autowired
    private DiaRepository reposity;

    @Override
    public List<DiaEntity> findAll() {
        return reposity.findAll();
    }

    @Override
    public DiaEntity findById(Integer id) {
        return reposity.findById(id).orElse(null);
    }


 

}
