package com.marcosdeDesarrollo.Ropa.Domain.Service;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.PermisoResponseDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.RolRequestDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.RolResponseDto;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Auditoria;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Permiso;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Rol;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Usuario;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.PermisoRepository;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.AuditoriaRepository;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.RolRepository;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PermisoRepository permisoRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaService auditoriaService;

    public RolService(RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PermisoRepository permisoRepository,
            AuditoriaRepository auditoriaRepository,
            AuditoriaService auditoriaService) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.permisoRepository = permisoRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<RolResponseDto> listarTodos() {
        return rolRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<RolResponseDto> buscarPorId(Integer id) {
        return rolRepository.findById(id).map(this::mapToResponse);
    }

    public RolResponseDto crear(RolRequestDto request) {
        validarNombreUnico(request.getNombre(), null);

        Rol rol = new Rol();
        rol.setNombre(normalizarNombre(request.getNombre()));
        rol.setDescripcion(request.getDescripcion());
        rol.setPermisos(new HashSet<>(obtenerPermisos(request.getPermisosIds())));

        Rol guardado = rolRepository.save(rol);
        auditoriaService.registrarInsert("roles", guardado.getId(), construirDatosAuditoria(guardado),
                "Creación de rol");
        return mapToResponse(guardado);
    }

    public Optional<RolResponseDto> actualizar(Integer id, RolRequestDto request) {
        return rolRepository.findById(id).map(rol -> {
            validarNombreUnico(request.getNombre(), id);
            Map<String, Object> datosAnteriores = construirDatosAuditoria(rol);
            rol.setNombre(normalizarNombre(request.getNombre()));
            rol.setDescripcion(request.getDescripcion());
            rol.setPermisos(new HashSet<>(obtenerPermisos(request.getPermisosIds())));
            Rol actualizado = rolRepository.save(rol);
            auditoriaService.registrarUpdate("roles", actualizado.getId(), datosAnteriores,
                    construirDatosAuditoria(actualizado), "Actualización de rol");
            return mapToResponse(actualizado);
        });
    }

    private RolResponseDto mapToResponse(Rol rol) {
        RolResponseDto dto = new RolResponseDto();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        dto.setFechaCreacion(rol.getFechaCreacion());
        List<Usuario> usuariosRol = usuarioRepository.findByRol_Id(rol.getId());
        dto.setUsuariosAsignados(usuariosRol.size());
        dto.setUsuarios(mapearNombresUsuarios(usuariosRol));
        dto.setUsuariosActivos(usuariosRol.stream()
                .filter(u -> "Activo".equalsIgnoreCase(u.getEstado()))
                .count());
        dto.setUsuariosInactivos(usuariosRol.stream()
                .filter(u -> "Inactivo".equalsIgnoreCase(u.getEstado()))
                .count());
        dto.setPermisos(mapPermisos(rol.getPermisos()));
        Auditoria ultimaAuditoria = obtenerAuditoriaReciente(rol.getId());
        LocalDateTime ultimaActualizacion = (ultimaAuditoria != null
                && "UPDATE".equalsIgnoreCase(ultimaAuditoria.getTipoOperacion()))
                ? ultimaAuditoria.getFecha() : null;
        dto.setUltimaActualizacion(ultimaActualizacion != null ? ultimaActualizacion : rol.getFechaCreacion());
        dto.setActualizadoPor(obtenerNombreUsuario(ultimaAuditoria));
        return dto;
    }

    private void validarNombreUnico(String nombre, Integer id) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        String normalizado = normalizarNombre(nombre);
        boolean existe = (id == null)
                ? rolRepository.existsByNombreIgnoreCase(normalizado)
                : rolRepository.existsByNombreIgnoreCaseAndIdNot(normalizado, id);
        if (existe) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre especificado");
        }
    }

    private String normalizarNombre(String nombre) {
        return nombre != null ? nombre.trim().toUpperCase() : null;
    }

    private List<Permiso> obtenerPermisos(List<Integer> permisosIds) {
        if (permisosIds == null || permisosIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Permiso> permisos = permisoRepository.findAllById(permisosIds);
        if (permisos.size() != new HashSet<>(permisosIds).size()) {
            throw new IllegalArgumentException("Uno o más permisos especificados no existen");
        }
        return permisos;
    }

    private List<PermisoResponseDto> mapPermisos(Set<Permiso> permisos) {
        if (permisos == null || permisos.isEmpty()) {
            return Collections.emptyList();
        }
        return permisos.stream()
                .sorted(Comparator.comparing(Permiso::getNombre, String.CASE_INSENSITIVE_ORDER))
                .map(permiso -> {
                    PermisoResponseDto dto = new PermisoResponseDto();
                    dto.setId(permiso.getId());
                    dto.setNombre(permiso.getNombre());
                    dto.setDescripcion(permiso.getDescripcion());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Auditoria obtenerAuditoriaReciente(Integer rolId) {
        return auditoriaRepository
                .findTopByTablaAfectadaAndIdRegistroOrderByFechaDesc("roles", rolId)
                .orElse(null);
    }

    private Map<String, Object> construirDatosAuditoria(Rol rol) {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("id", rol.getId());
        datos.put("nombre", rol.getNombre());
        datos.put("descripcion", rol.getDescripcion());
        datos.put("permisos", rol.getPermisos() == null ? Collections.emptyList()
                : rol.getPermisos()
                        .stream()
                        .sorted(Comparator.comparing(Permiso::getNombre, String.CASE_INSENSITIVE_ORDER))
                        .map(Permiso::getNombre)
                        .collect(Collectors.toList()));
        datos.put("fechaCreacion", rol.getFechaCreacion());
        return datos;
    }

    private String obtenerNombreUsuario(Auditoria auditoria) {
        if (auditoria == null) {
            return "Sistema";
        }
        Integer idUsuario = auditoria.getIdUsuario();
        if (idUsuario == null || idUsuario == 0) {
            return "Sistema";
        }
        return usuarioRepository.findById(idUsuario)
                .map(Usuario::getNombreUsuario)
                .orElse("Usuario " + idUsuario);
    }

    private List<String> mapearNombresUsuarios(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(usuario -> {
                    String nombre = usuario.getNombreUsuario();
                    return (nombre != null && !nombre.isBlank()) ? nombre : usuario.getEmail();
                })
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }
}
