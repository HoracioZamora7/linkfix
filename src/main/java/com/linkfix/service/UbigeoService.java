package com.linkfix.service;

import com.linkfix.entity.UbigeoDistritosEntity;
import java.util.List;
import java.util.Optional;


public interface UbigeoService {
    List<UbigeoDistritosEntity> listAll();
    Optional<UbigeoDistritosEntity> findById(String id);
    List<UbigeoDistritosEntity> findByProvinceId(String provinceId);
}
