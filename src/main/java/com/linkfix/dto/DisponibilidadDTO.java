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
public class DisponibilidadDTO {

    private int idDia;
    private String nombreDia;
    private List<String> horasDisponible;
}
