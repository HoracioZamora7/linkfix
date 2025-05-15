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


@Entity(name = "UsuarioRolEntity")
@Table(name = "UsuarioRol")


public class UsuarioRolEntity {

    
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
