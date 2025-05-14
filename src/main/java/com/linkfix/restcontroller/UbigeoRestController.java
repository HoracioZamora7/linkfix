package com.linkfix.restcontroller;

import com.linkfix.entity.UbigeoDistritosEntity;
import com.linkfix.service.UbigeoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/distrito")
public class UbigeoRestController {

    @Autowired
    private UbigeoService service;

    //Listar todos los distritos
    @GetMapping("/lista")
    public List<UbigeoDistritosEntity> listAll() {
        return service.listAll();
    }

    //Buscar distrito por id
    @GetMapping("/buscar")
    public Optional<UbigeoDistritosEntity> findById(@RequestParam String id) 
    {
        return service.findById(id);
    }

    //Listar distritos por provincia id
    @GetMapping("/listarPorProvincia")
    public List<UbigeoDistritosEntity> findByProvinceId(@RequestParam String provinceId) 
    {
        return service.findByProvinceId(provinceId);
    }
}
