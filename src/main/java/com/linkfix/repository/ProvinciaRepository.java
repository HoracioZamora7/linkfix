package com.linkfix.repository;

import com.linkfix.entity.ProvinciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinciaRepository extends JpaRepository<ProvinciaEntity, String> {
    /* listar por departamentou */
    @Query("SELECT p FROM ProvinciaEntity p WHERE p.departmentId = :deptId")
    List<ProvinciaEntity> findByDepartamentoId(@Param("deptId") String deptId);
}
