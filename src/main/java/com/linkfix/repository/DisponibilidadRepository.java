package com.linkfix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.DisponibilidadEntity;

@Repository
public interface DisponibilidadRepository extends JpaRepository<DisponibilidadEntity, Long> {



    

}
