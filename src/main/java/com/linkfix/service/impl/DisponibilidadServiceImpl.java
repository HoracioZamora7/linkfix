package com.linkfix.service.impl;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.DiaEntity;
import com.linkfix.entity.DisponibilidadEntity;
import com.linkfix.entity.UsuarioEntity;
import com.linkfix.repository.DisponibilidadRepository;
import com.linkfix.service.DiaService;
import com.linkfix.service.DisponibilidadService;
import com.linkfix.service.UsuarioService;

@Service
public class DisponibilidadServiceImpl implements DisponibilidadService{

    @Autowired
    private DisponibilidadRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DiaService diaService;

    private static final int INCOMPATBILIDAD = 1;
    private static final int GUARDADO_CON_EXITO = 2;

    @Override
    public List<DisponibilidadEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<List<DisponibilidadEntity>> findByIdTecnico(Long id) {
       return repository.findByIdTecnico(id);
    }

    @Override
    public Optional<List<DisponibilidadEntity>> findByDia(Integer id) {
        return repository.findByDia(id);
    }

    @Override
    public Optional<List<DisponibilidadEntity>> revisarHorario(LocalTime horaInicio, LocalTime horaFin, Integer idDia, Long usuarioID) {
        return repository.revisarHorario(horaInicio, horaFin, idDia, usuarioID);
    }

    @Override
    public Boolean deleteById(Long id, Long usuarioID) {
        try {
            DisponibilidadEntity disponibilidadEntity = repository.findById(id).orElse(null);
            if(disponibilidadEntity.getUsuario().getId()==usuarioID){
                repository.deleteById(id);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
        
    }

    @Override
    public Integer save(LocalTime horaInicio, LocalTime horaFin, Integer idDia, Long usuarioID) {
        
        DisponibilidadEntity disponibilidad = new DisponibilidadEntity();

        Optional<List<DisponibilidadEntity>> conflictos = revisarHorario(horaInicio, horaFin, idDia, usuarioID);
        if (conflictos.isPresent() && !conflictos.get().isEmpty()) {
            return INCOMPATBILIDAD;
        }
        
        UsuarioEntity usuario = usuarioService.findById(usuarioID);
        disponibilidad.setUsuario(usuario);
        disponibilidad.setHoraInicio(horaInicio);
        disponibilidad.setHoraFin(horaFin);
        disponibilidad.setDia(diaService.findById(idDia));
        
        repository.save(disponibilidad);

        return GUARDADO_CON_EXITO;
    }

}
