package com.linkfix.service.impl;

import com.linkfix.entity.UbigeoDistritosEntity;
import com.linkfix.repository.UbigeoDistritosRepository;
import com.linkfix.service.UbigeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UbigeoServiceImpl implements UbigeoService {

    @Autowired
    private UbigeoDistritosRepository repository;

    @Override
    public List<UbigeoDistritosEntity> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UbigeoDistritosEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<UbigeoDistritosEntity> findByProvinceId(String provinceId) 
    {
        // TODO Auto-generated method stub
        return repository.findByProvinceId(provinceId);
    }
}
