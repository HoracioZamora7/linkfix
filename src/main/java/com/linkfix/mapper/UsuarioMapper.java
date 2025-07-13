package com.linkfix.mapper;

public class UsuarioMapper {
/*  esto se paso al service de usuario como metodo publico
    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private ProvinciaService provinciaService;

    public UsuarioDTO toSessionUsuarioDTO(UsuarioEntity usuario, List<RolEntity> roles) {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setCorreo(usuario.getCorreo());
        dto.setNombre(usuario.getPersona().getNombre());
        dto.setApellidos(usuario.getPersona().getApellidos());
        dto.setDni(usuario.getPersona().getDni());
        dto.setDireccion(usuario.getPersona().getDireccion());
        dto.setUbigeo(usuario.getPersona().getUbigeo().getId());
        dto.setCalificacion(usuario.getCalificacion());
        dto.setFecha_registro(usuario.getFecha_registro());
        dto.setTelefono(usuario.getPersona().getTelefono());

        //temporal
        DepartamentoEntity departamento = departamentoService.findById(usuario.getPersona().getUbigeo().getDepartmentId()).orElse(null);
        ProvinciaEntity provincia = provinciaService.findById(usuario.getPersona().getUbigeo().getProvinceId()).orElse(null);
        dto.setDepartamento(departamento.getName());
        dto.setProvincia(provincia.getName());
        dto.setDistrito(usuario.getPersona().getUbigeo().getName());
        //
        
        List<Integer> rolesId = roles.stream()
                                    .map(RolEntity::getId) 
                                    .collect(Collectors.toList());
        dto.setRoles(rolesId);

        List<String> rolesNombre = roles.stream()
                                        .map(RolEntity::getNombre)
                                        .collect(Collectors.toList());
        dto.setNombresRoles(rolesNombre);

        return dto;
    } */
}
