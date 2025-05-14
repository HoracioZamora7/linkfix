package com.linkfix.service;

import com.linkfix.entity.ubigeo_distritosEntity;
import java.util.List;
import java.util.Optional;


public interface UbigeoService {
    List<ubigeo_distritosEntity> listAll();
    Optional<ubigeo_distritosEntity> findById(String id);
    List<ubigeo_distritosEntity> findByProvinceId(String provinceId);
}
