package com.marcosdeDesarrollo.Ropa.Web.Controller;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.PermisoResponseDto;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Permiso;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.PermisoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permisos")
@CrossOrigin(origins = "*")
@Tag(name = "Permisos", description = "Endpoints para consulta de permisos del sistema") // 👈 NUEVO
public class PermisoController {

    private final PermisoRepository permisoRepository;

    public PermisoController(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    @Operation(
        summary = "Listar todos los permisos", 
        description = "Obtiene la lista completa de permisos disponibles en el sistema para asignación de roles"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = PermisoResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<PermisoResponseDto> listar() {
        return permisoRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PermisoResponseDto mapToDto(Permiso permiso) {
        PermisoResponseDto dto = new PermisoResponseDto();
        dto.setId(permiso.getId());
        dto.setNombre(permiso.getNombre());
        dto.setDescripcion(permiso.getDescripcion());
        return dto;
    }
}