package com.marcosdeDesarrollo.demo.Service;

import com.marcosdeDesarrollo.demo.Entity.Estado;
import com.marcosdeDesarrollo.demo.Entity.Producto;
import com.marcosdeDesarrollo.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
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

    // NUEVO MÉTODO QUE DEBES AÑADIR - PARA EL POST
    public Producto guardarProducto(Producto producto) {
        try {
            // Validaciones básicas
            if (producto.getSku() == null || producto.getSku().trim().isEmpty()) {
                throw new IllegalArgumentException("El SKU es obligatorio");
            }
            
            // Limpiar el SKU
            String skuLimpio = producto.getSku().trim();
            producto.setSku(skuLimpio);
            
            // DEBUG detallado
            System.out.println("=== DEBUG GUARDAR PRODUCTO ===");
            System.out.println("SKU a guardar: '" + skuLimpio + "'");
            System.out.println("Nombre a guardar: '" + producto.getNombreProducto() + "'");
            
            // Verificar si el SKU ya existe
            boolean skuExiste = productoRepository.existsBySku(skuLimpio);
            System.out.println("¿SKU existe en BD? " + skuExiste);
            
            if (skuExiste) {
                Producto existente = productoRepository.findBySkuIgnoreCase(skuLimpio);
                throw new IllegalArgumentException("El SKU '" + skuLimpio + "' ya existe. Está siendo usado por: " + existente.getNombreProducto());
            }
            
            System.out.println("✅ SKU válido, guardando producto...");
            return productoRepository.save(producto);
            
        } catch (IllegalArgumentException e) {
            // Manejo de errores específicos
            System.out.println("❌ Error al guardar producto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Captura de errores generales
            System.out.println("❌ Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error inesperado al guardar el producto", e);
        }
    }
}