package com.linkfix.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>
{
    @Query("select u from UsuarioEntity u where u.estado.id = 1")
    List<UsuarioEntity> findAllCustom();
    List<UsuarioEntity> findAll();
    
}
