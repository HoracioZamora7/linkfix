package com.linkfix.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linkfix.entity.EspecialidadEntity;

@Repository
public interface EspecialidadRepository extends JpaRepository<EspecialidadEntity, Long> {

    @Query("SELECT e FROM EspecialidadEntity e WHERE e.usuario.id = :idTecnico")
    Optional<List<EspecialidadEntity>> findByIdTecnico(@Param("idTecnico") Long id);  

}
