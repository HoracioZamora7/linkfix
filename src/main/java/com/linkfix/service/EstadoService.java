package com.linkfix.service;

import java.util.List;
import com.linkfix.entity.EstadoEntity;

public interface EstadoService {

    List<EstadoEntity> findAll();

    EstadoEntity findById(int id);

    EstadoEntity findByNombre(String nombre);    
}
