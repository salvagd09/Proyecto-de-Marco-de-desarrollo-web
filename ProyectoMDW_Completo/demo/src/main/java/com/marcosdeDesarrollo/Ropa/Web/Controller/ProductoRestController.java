package com.marcosdeDesarrollo.Ropa.Web.Controller;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.ActualizarEstadoProductoRequest;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.ProductoRequestDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.ProductoResponseDto;
import com.marcosdeDesarrollo.Ropa.Domain.Service.ProductoService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Endpoints para gestión completa de productos") 
public class ProductoRestController {

    private final ProductoService productoService;

    ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(
        summary = "Obtener productos con filtros", 
        description = "Retorna lista de productos con opciones de filtrado por estado, stock, precio, categoría y búsqueda textual"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o expirado")
    })
    // Endpoint para obtener productos con filtros
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> obtenerProductos(
            @Parameter(description = "Filtrar por estado: Activo/Inactivo", example = "Activo") 
            @RequestParam(required = false) String estado,
            
            @Parameter(description = "Filtrar por nivel de stock: bajo/normal/alto", example = "bajo") 
            @RequestParam(required = false) String stock,
            
            @Parameter(description = "Ordenar por precio: ascendente/descendente", example = "ascendente") 
            @RequestParam(required = false) String precio,
            
            @Parameter(description = "ID de categoría para filtrar", example = "1") 
            @RequestParam(required = false) Long categoriaId,
            
            @Parameter(description = "Término de búsqueda en nombre, descripción o SKU", example = "polo") 
            @RequestParam(required = false, name = "search") String terminoBusqueda) {

        return ResponseEntity.ok(
                productoService.obtenerProductosFiltrados(estado, stock, precio, categoriaId, terminoBusqueda));
    }

    @Operation(
        summary = "Obtener estadísticas de productos", 
        description = "Retorna métricas y estadísticas generales del inventario de productos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })

    // Endpoint para obtener estadísticas de productos
    @GetMapping("/estadisticas")
    public Map<String, Long> obtenerEstadisticasProductos() {
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalProductos", productoService.contarTotalProductos());
        estadisticas.put("productosActivos", productoService.contarProductosActivos());
        estadisticas.put("stockBajo", productoService.contarStockBajo());
        estadisticas.put("productosInactivos", productoService.contarProductosInactivos());
        return estadisticas;
    }

    @Operation(
        summary = "Crear nuevo producto", 
        description = "Crea un nuevo producto en el catálogo del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", 
                    content = @Content(schema = @Schema(implementation = ProductoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    // Endpoint para crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> crearProducto(
            @Parameter(description = "Datos del producto a crear", required = true)
            @RequestBody ProductoRequestDto productoRequest) {
        try {
            ProductoResponseDto nuevoProducto = productoService.guardarProducto(productoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo crear el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
        summary = "Actualizar producto existente", 
        description = "Actualiza completamente la información de un producto específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ProductoResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o ID no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    // Endpoint para actualizar un producto existente
    @PutMapping("/{idProducto}")
    public ResponseEntity<?> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable Long idProducto,
            
            @Parameter(description = "Nuevos datos del producto", required = true)
            @RequestBody ProductoRequestDto productoRequest) {
        try {
            ProductoResponseDto productoActualizado = productoService.actualizarProducto(idProducto, productoRequest);
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo actualizar el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
        summary = "Cambiar estado de producto", 
        description = "Actualiza el estado de un producto (Activo/Inactivo) - eliminación lógica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del producto actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado no válido o ID no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    // Endpoint para cambiar el estado de un producto (eliminación lógica)
    @PatchMapping("/{idProducto}/estado")
    public ResponseEntity<?> actualizarEstadoProducto(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable Long idProducto,
            
            @Parameter(description = "Solicitud con nuevo estado", required = true)
            @RequestBody ActualizarEstadoProductoRequest request) {
        try {
            if (request == null || request.getEstado() == null) {
                throw new IllegalArgumentException("El nuevo estado es obligatorio");
            }
            ProductoResponseDto productoActualizado = productoService.actualizarEstadoProducto(idProducto, request.getEstado());
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo actualizar el estado del producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}