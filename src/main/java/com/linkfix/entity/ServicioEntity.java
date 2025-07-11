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

@Entity(name = "ServicioEntity")
@Table(name = "Servicio")

public class ServicioEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "idTecnico")
    private UsuarioEntity tecnico;

    @ManyToOne
    @JoinColumn(name = "idElectrodomestico")
    private ElectrodomesticoEntity electrodomestico;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fecha_solicitud;

    @Column(name = "fecha_visita")
    private LocalDateTime fecha_visita;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fecha_finalizacion;

    @ManyToOne
    @JoinColumn(name = "idEstado")
    private EstadoEntity estado;

    @Column(name = "calificacion")
    private float calificacion;

    @Column(name="comentario")
    private String comentario;

}
