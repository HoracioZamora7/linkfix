package com.linkfix.service.impl;

import com.linkfix.entity.DepartamentoEntity;
import com.linkfix.repository.DepartamentoRepository;
import com.linkfix.service.DepartamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    @Override
    public List<DepartamentoEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DepartamentoEntity> findById(String id) {
        return repository.findById(id);
    }
}
