package com.linkfix.service.aud;

import com.linkfix.entity.aud.AUDUsuarioHistorial;
import com.linkfix.repository.aud.AUDUsuarioHistorialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AUDUsuarioHistorialService {

    @Autowired
    private AUDUsuarioHistorialRepository repository;

    public Page<AUDUsuarioHistorial> findByIdUsuario(Long idUsuario, Pageable pageable)
    {
        return repository.findByIdUsuario(idUsuario, pageable);
    }

    
    public Page<AUDUsuarioHistorial> findAll(Pageable pageable)
    {
        return repository.findAll(pageable);
    }

    
    public Page<AUDUsuarioHistorial> findByCorreo(String correo, Pageable pageable)
    {
        return repository.findByCorreo(correo, pageable);
    }
}
