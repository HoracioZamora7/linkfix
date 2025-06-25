package com.linkfix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.entity.UsuarioRolEntity;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Integer> {

    List<UsuarioRolEntity> findAll();

    @Query("SELECT ur.rol FROM UsuarioRolEntity ur WHERE ur.usuario = :usuario")
    List<RolEntity> findRolesByUsuario(UsuarioEntity usuario);


    @Query("SELECT ur.rol FROM UsuarioRolEntity ur WHERE ur.usuario.id = :idUsuario")
    List<RolEntity> findRolesByIdUsuario(@Param("idUsuario") Long id);

}
