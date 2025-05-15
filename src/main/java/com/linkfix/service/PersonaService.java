package com.linkfix.service;

import java.util.List;

import com.linkfix.entity.PersonaEntity;

public interface PersonaService {
    public List<PersonaEntity> findAll();
    public PersonaEntity save(PersonaEntity p);
}
