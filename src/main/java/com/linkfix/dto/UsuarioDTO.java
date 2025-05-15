package com.linkfix.dto;

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
}
