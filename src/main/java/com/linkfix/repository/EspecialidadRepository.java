package com.linkfix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.EspecialidadEntity;

@Repository
public interface EspecialidadRepository extends JpaRepository<EspecialidadEntity, Long> {



    

}
