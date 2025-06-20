package com.linkfix.controller;

import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.linkfix.dto.TecnicoListadoDTO;
import com.linkfix.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/test")
public class TestTecnicoController {

    private final UsuarioRepository usuarioRepository;

    public TestTecnicoController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/tecnicos")
    public Page<TecnicoListadoDTO> listarTecnicosDisponibles(@RequestParam(required = false) String idUbigeo, @RequestParam(required = false) Long idElectrodomestico, @RequestParam(required = false) Integer idDia, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) 
    {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.listarTecnicosDisponibles(idUbigeo, idElectrodomestico, idDia, horaInicio, horaFin, pageable);
    }
}
