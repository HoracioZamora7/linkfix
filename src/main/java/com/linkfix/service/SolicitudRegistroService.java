package com.linkfix.service;

import java.util.List;
import com.linkfix.entity.SolicitudRegistroEntity;

public interface SolicitudRegistroService {

    SolicitudRegistroEntity save(SolicitudRegistroEntity s);

    SolicitudRegistroEntity update(SolicitudRegistroEntity s);

    List<SolicitudRegistroEntity> findAll();

    SolicitudRegistroEntity findById(Long id);

    List<SolicitudRegistroEntity> findAllByEstado(boolean estado);
}
