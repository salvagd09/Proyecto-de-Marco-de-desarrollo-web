package com.marcosdeDesarrollo.demo.Service;

import com.marcosdeDesarrollo.demo.Entity.Estado;
import com.marcosdeDesarrollo.demo.Entity.Producto;
import com.marcosdeDesarrollo.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Método para obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    // Método para obtener productos activos
    public long contarProductosActivos() {
        return productoRepository.countByEstado(Estado.Activo);
    }

    // Método para obtener productos con stock bajo (menor de 20)
    public long contarStockBajo() {
        return productoRepository.countByStockActualLessThan(20);
    }

     // Método para contar el total de productos
     public long contarTotalProductos() {
        return productoRepository.count();
    }
}