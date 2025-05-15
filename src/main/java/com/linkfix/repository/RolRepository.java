package com.linkfix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.linkfix.entity.RolEntity;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Integer> {
    RolEntity findByNombre(String nombre); 
}
