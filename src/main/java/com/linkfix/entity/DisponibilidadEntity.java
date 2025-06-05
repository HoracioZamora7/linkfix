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

@Entity(name = "DisponibilidadEntity")
@Table(name = "Disponibilidad")

public class DisponibilidadEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idTecnico")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idDia")
    private DiaEntity dia;

    @Column(name = "hora_inicio")
    private LocalDateTime hora_inicio;

    @Column(name = "hora_fin")
    private LocalDateTime hora_fin;
}
