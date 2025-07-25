package com.linkfix.repository;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.linkfix.dto.ListadoUsuariosDTO;
import com.linkfix.dto.TecnicoListadoDTO;
import com.linkfix.entity.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>
{
    //listar todos los usuarios de estado 1
    @Query("SELECT u FROM UsuarioEntity u WHERE u.estado.id = 1")
    List<UsuarioEntity> findAllCustom();

    //listar todos los usuarios
    List<UsuarioEntity> findAll();

    //buscar usuario por correo
    UsuarioEntity findByCorreo(String correo);

    //buscar usuario por token de email
    @Query("SELECT u FROM UsuarioEntity u WHERE u.emailToken = :token")
    UsuarioEntity findByEmailToken(@Param("token") String token);    

    //listar usuarios de rol tecnico. Filtros opcionales: 
    /* @Query(value = "call listarTecnicosDisponibles(:idUbigeo, :idElectrodomestico, :idDia, :horaInicio, :horaFin, :page, :pageSize) ", nativeQuery = true) 
    List<UsuarioEntity> listarTecnicosDisponibles(@Param("idUbigeo") String idUbigeo, @Param("idElectrodomestico") Long idElectrodomestico, @Param("idDia") Integer idDia, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin, @Param("page") int page, @Param("pageSize") int pageSize);
 */

    //llamar al procedure aca

   /*  @Procedure(procedureName = "listarTecnicosDisponibles")
    Page<TecnicoListadoDTO> listarTecnicosDisponibles(); */

   @Query(value = """
    SELECT 
        u.id AS id,
        CONCAT(p.apellidos, ', ', p.nombre) AS nombreCompleto,
        u.correo AS correo,
        u.calificacion AS calificacion,
        MAX(d.nombre) AS nombreDia,
        MIN(disp.horaInicio) AS horaInicio,
        MAX(disp.horaFin) AS horaFin,
        CASE 
            WHEN :idElectrodomestico IS NULL THEN 0 
            WHEN EXISTS (
                SELECT 1 FROM especialidad esp
                WHERE esp.idTecnico = u.id AND esp.idElectrodomestico = :idElectrodomestico
            ) THEN 1
            ELSE 0
        END AS tieneEspecialidad
    FROM usuario u
    JOIN persona p ON u.idPersona = p.id
    LEFT JOIN disponibilidad disp ON disp.idTecnico = u.id
    LEFT JOIN dia d ON d.id = disp.idDia
    LEFT JOIN usuariorol ur ON ur.idUsuario = u.id
    WHERE u.idEstado = 1
    AND ur.idRol = 3
    AND (:idUbigeo IS NULL OR SUBSTRING(p.idUbigeo, 1, 4) = SUBSTRING(:idUbigeo, 1, 4))
    AND (:idDia IS NULL OR disp.idDia = :idDia)
    AND (:horaInicio IS NULL OR :horaFin IS NULL OR 
         (disp.horaInicio <= :horaFin AND disp.horaFin >= :horaInicio))
    GROUP BY u.id, p.apellidos, p.nombre, u.correo, u.calificacion
    ORDER BY 
        CASE 
            WHEN :idElectrodomestico IS NULL THEN 0
            WHEN EXISTS (
                SELECT 1 FROM especialidad esp
                WHERE esp.idTecnico = u.id AND esp.idElectrodomestico = :idElectrodomestico
            ) THEN 0
            ELSE 1
        END,
        u.calificacion DESC
    """,
    countQuery = """
        SELECT COUNT(DISTINCT u.id)
        FROM usuario u
        JOIN persona p ON u.idPersona = p.id
        LEFT JOIN disponibilidad disp ON disp.idTecnico = u.id
        WHERE u.idEstado = 1
        AND (:idUbigeo IS NULL OR SUBSTRING(p.idUbigeo, 1, 4) = SUBSTRING(:idUbigeo, 1, 4))
        AND (:idDia IS NULL OR disp.idDia = :idDia)
        AND (:horaInicio IS NULL OR :horaFin IS NULL OR 
            (disp.horaInicio <= :horaFin AND disp.horaFin >= :horaInicio))
    """,
    nativeQuery = true)
    Page<TecnicoListadoDTO> listarTecnicosDisponibles(
        @Param("idUbigeo") String idUbigeo,
        @Param("idElectrodomestico") Long idElectrodomestico,
        @Param("idDia") Integer idDia,
        @Param("horaInicio") LocalTime horaInicio,
        @Param("horaFin") LocalTime horaFin,
        Pageable pageable
    );

   @Query(value = """
            SELECT u.id, u.idPersona, u.correo, '' AS roles, u.calificacion, e.nombre, u.fecha_registro, p.dni, p.ruc, p.nombre, p.apellidos, p.direccion, p.telefono, p.idUbigeo
            FROM usuario u
            LEFT JOIN persona p ON p.id = u.idPersona
            LEFT JOIN estado e ON e.id = u.idEstado
            WHERE u.correo LIKE CONCAT('%', :correo, '%')
            """,
            nativeQuery = true)
            Page<ListadoUsuariosDTO> listarUsuarios(@Param("correo") String correo, Pageable pageable);

    @Query("SELECT AVG(s.calificacion) FROM ServicioEntity s WHERE s.tecnico.id = :idTecnico AND s.estado.id = 7 AND s.calificacion IS NOT NULL")
    Float recalcularCalificacion(@Param("idTecnico") Long idTecnico);
           
}