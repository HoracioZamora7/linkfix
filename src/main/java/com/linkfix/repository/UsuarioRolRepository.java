package com.linkfix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.linkfix.entity.UsuarioRolEntity;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {

    List<UsuarioRolEntity> findAll();
}
