package com.linkfix.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.linkfix.dto.ListadoUsuariosDTO;
import com.linkfix.dto.TecnicoListadoDTO;
import com.linkfix.dto.UsuarioDTO;
import com.linkfix.entity.RolEntity;
import com.linkfix.entity.UsuarioEntity;

public interface UsuarioService {
    public List<UsuarioEntity> listAll();
    public UsuarioEntity registrar(UsuarioEntity u);
    public UsuarioEntity update(UsuarioEntity u);
    public boolean actualizarPerfil(UsuarioDTO usuarioDTO, Long idUsuarioUltimaEdicion);
    public UsuarioEntity findByCorreo(String c);
    public UsuarioEntity findById(Long id);
    public UsuarioEntity findByEmailToken(String token);
    public UsuarioEntity generarToken(UsuarioEntity u);
    public Page<ListadoUsuariosDTO> listarUsuarios(String correo, Pageable pageable);
    public UsuarioDTO toUsuarioDTOById(Long id);
    //mapeador
    public UsuarioDTO toSessionUsuarioDTO(UsuarioEntity usuario, List<RolEntity> roles);
    public boolean actualizarUsuario(UsuarioEntity usuarioEntity, Long idUsuarioUltimaEdicion);

    public Page<TecnicoListadoDTO> listarTecnicosDisponibles(String idUbigeo, Long idElectrodomestico, Integer idDia, LocalTime horaInicio, LocalTime horaFIn, Pageable pageable);

    public void recalcularCalificacion(Long idTecnico);
}