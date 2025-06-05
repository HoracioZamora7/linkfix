package com.linkfix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.IncidenciaEntity;

@Repository
public interface IncidenciaRepository extends JpaRepository<IncidenciaEntity, Long> {



    

}
