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
    private String correo;
    private float calificacion;
    private LocalDateTime fecha_registro;
    //esto se podría separar en personadto
    private String dni;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String ruc;

    //esto se podría separar en otro dto
    private List<Integer> roles;
    private List<String> nombresRoles;
    
    //esto podría ir dentro de ubigeo dto, a la vez dentro de personadto
    private String ubigeo;
    private String departamento;
    private String provincia;
    private String distrito;
    private String departamentoId;
    private String provinciaId;
    
}

