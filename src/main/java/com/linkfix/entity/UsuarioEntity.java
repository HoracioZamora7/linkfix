package com.linkfix.entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.Date;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity(name = "UsuarioEntity")
@Table(name = "Usuario")


public class UsuarioEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idPersona")
    private PersonaEntity persona;

    @Column(name = "correo", length = 50)
    private String correo;

    @Column(name = "contrasena", length = 255)
    private String contrasena;

    @Column(name = "calificacion")
    private float calificacion;

    @ManyToOne
    @JoinColumn(name = "idEstado")
    private EstadoEntity estado;

    @Column(name = "fecha_registro")
    private Date fecha_registro;

    @Transient
    private boolean tecnico;

    @Transient
    private boolean cliente;
}

