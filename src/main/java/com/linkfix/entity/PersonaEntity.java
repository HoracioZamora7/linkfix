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

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "dni")
    private String dni;

    @ManyToOne
    @JoinColumn(name = "idUbigeo")
    private Ubigeo_peru_districtsEntity ubigeo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;
}
