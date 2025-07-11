package com.linkfix.dto;
import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoListadoDTO {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private Float calificacion;
    private String nombreDia;
    private Time horaInicio;
    private Time horaFin;
    private int tieneEspecialidad;

    //podría haber una clase dto para persona, así evitamos campos repetidos aquí y en los demás dtos
}
