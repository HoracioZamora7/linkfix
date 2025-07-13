package com.linkfix.entity;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "IncidenciaEntity")
@Table(name = "Incidencia")

public class IncidenciaEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idServicio")
    private ServicioEntity servicio;

    @ManyToOne
    @JoinColumn(name="idAdmin")
    private UsuarioEntity administrador;

    @Column(name = "titulo", length = 50)
    private String titulo;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "fecha_registro")
    private LocalDateTime fecha_registro;

    @Column(name = "fecha_atencion")
    private LocalDateTime fecha_atencion;

    @ManyToOne
    @JoinColumn(name = "idEstado")
    private EstadoEntity estado;

}
