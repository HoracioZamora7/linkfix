package com.linkfix.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkfix.entity.PersonaEntity;
import com.linkfix.repository.PersonaRepository;
import com.linkfix.service.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService{


    @Autowired
    private PersonaRepository repository;

    @Override
    public List<PersonaEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public PersonaEntity save(PersonaEntity p) {
        return repository.save(p);
    }
}
