package com.linkfix.entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Entity(name = "PersonaEntity")
@Table(name = "Persona")
public class PersonaEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "apellidos", length = 50)
    private String apellidos;

    @Column(name = "dni", length = 8)
    private String dni;

    @ManyToOne
    @JoinColumn(name = "idUbigeo")
    private UbigeoDistritosEntity ubigeo;

    @Column(name = "telefono", length = 9)
    private String telefono;

    @Column(name = "direccion", length = 75)
    private String direccion;
}
