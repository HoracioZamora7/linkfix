package com.linkfix.service;

import java.util.List;
import com.linkfix.entity.RolEntity;

public interface RolService {

    List<RolEntity> findAll();

    RolEntity findById(int id);

    RolEntity findByNombre(String nombre);
}
