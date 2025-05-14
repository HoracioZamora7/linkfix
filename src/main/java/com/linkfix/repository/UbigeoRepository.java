package com.linkfix.repository;

import com.linkfix.entity.ubigeo_distritosEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbigeoRepository extends JpaRepository<ubigeo_distritosEntity, String> {
    
    /* lo mismo */
    /* List<ubigeo_distritosEntity> findByProvinceId(String provinceId); */

    /* listar distritos por idprovincia*/
    @Query("SELECT DISTINCT u FROM ubigeo_distritos u WHERE u.province_id = :provinceId")
    List<ubigeo_distritosEntity> findByProvinceId(@Param("provinceId") String provinceId);
}