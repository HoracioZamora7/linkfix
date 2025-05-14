package com.linkfix.service.impl;

import com.linkfix.entity.ProvinciaEntity;
import com.linkfix.repository.ProvinciaRepository;
import com.linkfix.service.ProvinciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinciaServiceImpl implements ProvinciaService {

    @Autowired
    private ProvinciaRepository repository;

    @Override
    public List<ProvinciaEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProvinciaEntity> findByDepartamentoId(String departmentId) {
        return repository.findByDepartamentoId(departmentId);
    }
}
