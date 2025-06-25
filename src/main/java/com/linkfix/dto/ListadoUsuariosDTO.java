package com.linkfix.dto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListadoUsuariosDTO {
    private long id;
    private long idPersona;
    private String correo;
    private String roles;
    private float calificacion;
    private String estado;
    private LocalDateTime fecha_registro;
    private String dni;
    private String ruc;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String ubigeo;
}
