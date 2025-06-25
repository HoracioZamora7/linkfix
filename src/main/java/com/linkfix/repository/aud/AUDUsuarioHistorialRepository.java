package com.linkfix.repository.aud;

import com.linkfix.entity.aud.AUDUsuarioHistorial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AUDUsuarioHistorialRepository extends JpaRepository<AUDUsuarioHistorial, Long> {
    Page<AUDUsuarioHistorial> findByIdUsuario(Long idUsuario, Pageable pageable);
    
    Page<AUDUsuarioHistorial> findByCorreo(String correo, Pageable pageable);
    Page<AUDUsuarioHistorial> findAll(Pageable Pageable);
}
