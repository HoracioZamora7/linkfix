package com.linkfix.service;

import com.linkfix.entity.ProvinciaEntity;
import java.util.List;
import java.util.Optional;

public interface ProvinciaService {
    List<ProvinciaEntity> findAll();
    List<ProvinciaEntity> findByDepartamentoId(String departmentId);
    Optional<ProvinciaEntity> findById(String id);
}
