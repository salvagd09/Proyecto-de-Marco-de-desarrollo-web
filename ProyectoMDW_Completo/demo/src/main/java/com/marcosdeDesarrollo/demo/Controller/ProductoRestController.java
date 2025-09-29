package com.marcosdeDesarrollo.demo.Controller;

import com.marcosdeDesarrollo.demo.DTO.ActualizarEstadoProductoRequest;
import com.marcosdeDesarrollo.demo.DTO.ProductoRequestDto;
import com.marcosdeDesarrollo.demo.DTO.ProductoResponseDto;
import com.marcosdeDesarrollo.demo.Service.ProductoService;
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
@CrossOrigin(origins = "*") // Para permitir requests desde tu frontend
public class ProductoRestController {

    private final ProductoService productoService;

    ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Endpoint para obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> obtenerProductos(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false) String precio,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false, name = "search") String terminoBusqueda) {

        return ResponseEntity.ok(
                productoService.obtenerProductosFiltrados(estado, stock, precio, categoriaId, terminoBusqueda));
    }

    // Endpoint para obtener las estadísticas de productos
    @GetMapping("/estadisticas")
    public Map<String, Long> obtenerEstadisticasProductos() {
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalProductos", productoService.contarTotalProductos());
        estadisticas.put("productosActivos", productoService.contarProductosActivos());
        estadisticas.put("stockBajo", productoService.contarStockBajo());
        estadisticas.put("productosInactivos", productoService.contarProductosInactivos());
        return estadisticas;
    }

    // NUEVO: Endpoint para crear un producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody ProductoRequestDto productoRequest) {
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

    @PutMapping("/{idProducto}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long idProducto,
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

    @PatchMapping("/{idProducto}/estado")
    public ResponseEntity<?> actualizarEstadoProducto(@PathVariable Long idProducto,
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