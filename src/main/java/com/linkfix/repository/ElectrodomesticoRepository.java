package com.linkfix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.ElectrodomesticoEntity;

@Repository
public interface ElectrodomesticoRepository extends JpaRepository<ElectrodomesticoEntity, Long> {



    

}
