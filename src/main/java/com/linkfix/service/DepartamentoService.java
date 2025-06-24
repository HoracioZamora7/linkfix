package com.linkfix.service;

import com.linkfix.entity.DepartamentoEntity;
import java.util.List;
import java.util.Optional;

public interface DepartamentoService {
    List<DepartamentoEntity> findAll();
    Optional<DepartamentoEntity> findById(String id);
}
