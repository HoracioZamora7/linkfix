package com.linkfix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.linkfix.entity.EstadoEntity;

public interface EstadoRepository extends JpaRepository<EstadoEntity, Integer> {
    List<EstadoEntity> findAll();

    EstadoEntity findByNombre(String nombre); 
}
