package com.marcosdeDesarrollo.demo.Controller;

import com.marcosdeDesarrollo.demo.Entity.Producto;
import com.marcosdeDesarrollo.demo.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Para permitir requests desde tu frontend
public class ProductoRestController {

    @Autowired
    private ProductoService productoService;

    // Endpoint para obtener todos los productos
    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoService.obtenerTodosLosProductos();
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
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.guardarProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo crear el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}