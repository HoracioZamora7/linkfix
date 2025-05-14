package com.linkfix.service;

import com.linkfix.entity.ProvinciaEntity;
import java.util.List;

public interface ProvinciaService {
    List<ProvinciaEntity> findAll();
    List<ProvinciaEntity> findByDepartamentoId(String departmentId);
}
