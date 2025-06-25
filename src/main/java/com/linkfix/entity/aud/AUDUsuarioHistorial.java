package com.linkfix.entity.aud;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="AUDUsuarioHistorial")
public class AUDUsuarioHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "correo")
    private String correo;

    @Column(name = "idEstado")
    private Integer idEstado;

    @Column(name= "nombreEstado")
    private String nombreEstado;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "emailToken")
    private String emailToken;

    @Column(name = "emailTokenFechaExpiracion")
    private LocalDateTime emailTokenFechaExpiracion;

    @Column(name = "idUsuarioUltimaEdicion")
    private Long idUsuarioUltimaEdicion;

    @Column(name = "correoUsuarioUltimaEdicion")
    private String correoUsuarioUltimaEdicion;

    @Column(name = "idPersona")
    private Long idPersona;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "dni")
    private String dni;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "idUbigeo")
    private String idUbigeo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;

    @Transient
    private String roles;
}