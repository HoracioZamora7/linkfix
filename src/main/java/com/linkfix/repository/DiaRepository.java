package com.linkfix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.DiaEntity;

@Repository
public interface DiaRepository extends JpaRepository<DiaEntity, Long> {



    

}
