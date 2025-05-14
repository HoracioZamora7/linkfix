package com.linkfix.entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity(name = "Usuario_rolEntity")
@Table(name = "Usuario_rol")


public class Usuario_rolEntity {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="idUsuario")
    private UsuarioEntity usuario;
    
    @ManyToOne
    @JoinColumn(name="idRol")
    private RolEntity rol;
}
