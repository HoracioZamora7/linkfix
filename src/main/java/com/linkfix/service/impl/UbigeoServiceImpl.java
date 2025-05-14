package com.linkfix.service.impl;

import com.linkfix.entity.ubigeo_distritosEntity;
import com.linkfix.repository.UbigeoRepository;
import com.linkfix.service.UbigeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UbigeoServiceImpl implements UbigeoService {

    @Autowired
    private UbigeoRepository repository;

    @Override
    public List<ubigeo_distritosEntity> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ubigeo_distritosEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<ubigeo_distritosEntity> findByProvinceId(String provinceId) {
        // TODO Auto-generated method stub
        return repository.findByProvinceId(provinceId);
    }

}
