package com.linkfix.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private long id;
    private String nombre;
    private String apellidos;
    private String correo;
    private List<Integer> roles;
    private String dni;
    private String direccion;
    private String ubigeo;
    private String departamento;
    private String provincia;
    private String distrito;
    private float calificacion;
    private LocalDateTime fecha_registro;
    private String telefono;
}

