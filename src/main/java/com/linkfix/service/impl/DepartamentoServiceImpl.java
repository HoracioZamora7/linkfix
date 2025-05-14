package com.linkfix.service.impl;

import com.linkfix.entity.DepartamentoEntity;
import com.linkfix.repository.DepartamentoRepository;
import com.linkfix.service.DepartamentoService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository repository;

    public DepartamentoServiceImpl(DepartamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DepartamentoEntity> findAll() {
        return repository.findAll();
    }
}
