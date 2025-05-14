package com.linkfix.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.linkfix.entity.DepartamentoEntity;

import com.linkfix.service.DepartamentoService;

@RestController
@RequestMapping("/api/departamento")
public class DepartamentoRestController {
    @Autowired
    private DepartamentoService service;

    @GetMapping("/lista")
    public List<DepartamentoEntity> findAll()
    {
        return service.findAll();
    }
}
