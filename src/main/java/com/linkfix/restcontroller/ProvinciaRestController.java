package com.linkfix.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
/* import org.springframework.http.ResponseEntity; */
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.linkfix.entity.ProvinciaEntity;
import com.linkfix.service.ProvinciaService;

@RestController
@RequestMapping("/api/provincia")
public class ProvinciaRestController {

    @Autowired
    private ProvinciaService service;

    @GetMapping("/listarPorDepartamento")
    public List<ProvinciaEntity> listarProvinciasPorDepartamento(@RequestParam String departmentId) 
    {
        return /* ResponseEntity.ok */(service.findByDepartamentoId(departmentId));
    }
}
