package com.marcosdeDesarrollo.demo.Controller;

import com.marcosdeDesarrollo.demo.Entity.Producto;
import com.marcosdeDesarrollo.demo.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
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
}