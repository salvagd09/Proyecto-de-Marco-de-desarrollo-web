package com.marcosdeDesarrollo.Ropa.Web.Controller;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.CategoriaDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.CategoriaRequestDto;
import com.marcosdeDesarrollo.Ropa.Domain.Service.CategoriaService;
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
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@Tag(name = "Categorias", description = "Endpoints para gestión de categorías de productos") // 👈 NUEVO
public class CategoriaController {

    private final CategoriaService categoriaService;

    CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(
        summary = "Obtener todas las categorías", 
        description = "Retorna la lista completa de categorías activas disponibles en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> obtenerTodasLasCategorias() {
        try {
            return ResponseEntity.ok(categoriaService.obtenerTodasLasCategorias());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
        summary = "Obtener categoría por ID", 
        description = "Busca y retorna una categoría específica mediante su ID único"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = CategoriaDto.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría a buscar", required = true, example = "1")
            @PathVariable Long id) {
        Optional<CategoriaDto> categoria = categoriaService.obtenerCategoriaPorId(id);
        return categoria.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nueva categoría", 
        description = "Registra una nueva categoría en el sistema para organizar productos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(schema = @Schema(implementation = CategoriaDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o categoría ya existe"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crearCategoria(
            @Parameter(description = "Datos de la categoría a crear", required = true)
            @RequestBody CategoriaRequestDto categoriaRequest) {
        try {
            CategoriaDto nuevaCategoria = categoriaService.guardarCategoria(categoriaRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo registrar la categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}