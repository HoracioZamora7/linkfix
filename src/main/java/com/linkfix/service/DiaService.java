package com.linkfix.service;
import java.util.List;

import com.linkfix.entity.DiaEntity;


public interface DiaService {

    List<DiaEntity> findAll();
    DiaEntity findById(Integer id);

}