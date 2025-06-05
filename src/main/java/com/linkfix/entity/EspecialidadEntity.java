package com.linkfix.entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.Date;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "EspecialidadEntity")
@Table(name = "Especialidad")

public class EspecialidadEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idTecnico")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idElectrodomestico")
    private ElectrodomesticoEntity electrodomestico;

}
