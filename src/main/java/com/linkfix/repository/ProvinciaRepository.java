package com.linkfix.repository;

import com.linkfix.entity.ProvinciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity, String> {
    List<ProvinciaEntity> findByDepartamentoId(String departmentId); /* listar por departamentou */
}
