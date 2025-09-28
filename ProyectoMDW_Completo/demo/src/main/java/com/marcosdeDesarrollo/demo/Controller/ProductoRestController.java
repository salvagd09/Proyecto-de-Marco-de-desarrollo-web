package com.marcosdeDesarrollo.demo.Controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<ProductoResponseDto>> obtenerProductos() {
        return ResponseEntity.ok(productoService.obtenerTodosLosProductos());
    }

    // Endpoint para obtener las estadísticas de productos
    @GetMapping("/estadisticas")
    public Map<String, Long> obtenerEstadisticasProductos() {
        Map<String, Long> estadisticas = new HashMap<>();
        estadisticas.put("totalProductos", productoService.contarTotalProductos());
        estadisticas.put("productosActivos", productoService.contarProductosActivos());
        estadisticas.put("stockBajo", productoService.contarStockBajo());
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
}
