package com.linkfix.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.PersonaEntity;
@Repository
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long>
{
    List<PersonaEntity> findAll();
}
