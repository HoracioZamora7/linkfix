package com.linkfix.service;
import java.util.List;
import com.linkfix.entity.EspecialidadEntity;


public interface EspecialidadService {

    List<EspecialidadEntity> findAll();
    List<EspecialidadEntity> findByIdTecnico(Long id);

}
