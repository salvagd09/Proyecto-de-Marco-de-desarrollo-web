package com.marcosdeDesarrollo.Ropa.Web.Controller;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.RolRequestDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.RolResponseDto;
import com.marcosdeDesarrollo.Ropa.Domain.Service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
@Tag(name = "Roles", description = "Endpoints para gestión de roles de usuario") // 👈 NUEVO
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @Operation(
        summary = "Listar todos los roles", 
        description = "Obtiene la lista completa de roles disponibles en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public List<RolResponseDto> listar() {
        return rolService.listarTodos();
    }

    @Operation(
        summary = "Obtener rol por ID", 
        description = "Busca y retorna un rol específico mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDto> obtenerPorId(
            @Parameter(description = "ID del rol a buscar", required = true, example = "1")
            @PathVariable Integer id) {
        return rolService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo rol", 
        description = "Crea un nuevo rol en el sistema. Solo disponible para administradores"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente",
                    content = @Content(schema = @Schema(implementation = RolResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Permisos insuficientes")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Datos del rol a crear", required = true)
            @RequestBody RolRequestDto request) {
        try {
            RolResponseDto nuevo = rolService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Actualizar rol existente", 
        description = "Actualiza la información de un rol específico. No permite cambiar roles del sistema base"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = RolResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o rol del sistema"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del rol a actualizar", required = true, example = "1")
            @PathVariable Integer id,
            
            @Parameter(description = "Nuevos datos del rol", required = true)
            @RequestBody RolRequestDto request) {
        try {
            return rolService.actualizar(id, request)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Intentar inactivar rol", 
        description = "Los roles no pueden eliminarse ni desactivarse por políticas de seguridad del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "405", description = "Método no permitido - Los roles son permanentes"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PutMapping("/{id}/inactivar")
    public ResponseEntity<Map<String, String>> inactivarNoDisponible(
            @Parameter(description = "ID del rol", required = true, example = "1")
            @PathVariable Integer id) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Los roles no pueden eliminarse ni desactivarse desde la aplicación.");
        body.put("motivo", "Política de seguridad: Los roles son elementos permanentes del sistema.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(body);
    }
}