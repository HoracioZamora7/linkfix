package com.linkfix.repository;

import com.linkfix.entity.UbigeoDistritosEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbigeoDistritosRepository extends JpaRepository<UbigeoDistritosEntity, String> {
    
    /* lo mismo */
    /* List<ubigeo_distritosEntity> findByProvinceId(String provinceId); */

    /* listar distritos por idprovincia*/
    @Query("SELECT DISTINCT u FROM UbigeoDistritosEntity u WHERE u.provinceId = :provinceId")
    List<UbigeoDistritosEntity> findByProvinceId(@Param("provinceId") String provinceId);
}