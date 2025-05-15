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


@Entity
@Table(name = "SolicitudRegistro")
public class SolicitudRegistroEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="idTecnico")
    private UsuarioEntity tecnico;

    @Column(name = "fecha_registro")
    private LocalDateTime fecha_registro;

    @Column(name = "fecha_revision")
    private LocalDateTime fecha_revision;

    @ManyToOne
    @JoinColumn(name="idAdmin")
    private UsuarioEntity administrador;

    @Column(name="comentario", length = 120)
    private String comentario;

    @Column(name = "estado")
    private boolean estado;
}
