package com.linkfix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.linkfix.entity.SolicitudRegistroEntity;

@Repository
public interface SolicitudRegistroRepository extends JpaRepository<SolicitudRegistroEntity, Long> {

    List<SolicitudRegistroEntity> findAllByEstado(boolean estado);
}
