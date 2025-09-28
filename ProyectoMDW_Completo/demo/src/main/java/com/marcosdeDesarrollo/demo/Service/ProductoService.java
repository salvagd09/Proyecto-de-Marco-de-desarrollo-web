package com.marcosdeDesarrollo.demo.Service;

import com.marcosdeDesarrollo.demo.DTO.CategoriaDto;
import com.marcosdeDesarrollo.demo.DTO.ProductoRequestDto;
import com.marcosdeDesarrollo.demo.DTO.ProductoResponseDto;
import com.marcosdeDesarrollo.demo.Entity.Categoria;
import com.marcosdeDesarrollo.demo.Entity.Estado;
import com.marcosdeDesarrollo.demo.Entity.Producto;
import com.marcosdeDesarrollo.demo.Repository.CategoriaRepository;
import com.marcosdeDesarrollo.demo.Repository.ProductoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProductoResponseDto> obtenerTodosLosProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public long contarProductosActivos() {
        return productoRepository.countByEstado(Estado.Activo);
    }

    public long contarStockBajo() {
        return productoRepository.countByStockActualLessThan(20);
    }

    public long contarTotalProductos() {
        return productoRepository.count();
    }

    public ProductoResponseDto guardarProducto(ProductoRequestDto productoDto) {
        try {
            if (productoDto == null) {
                throw new IllegalArgumentException("Los datos del producto son obligatorios");
            }

            if (productoDto.getSku() == null || productoDto.getSku().trim().isEmpty()) {
                throw new IllegalArgumentException("El SKU es obligatorio");
            }

            if (productoDto.getCategoriaId() == null || productoDto.getCategoriaId() <= 0) {
                throw new IllegalArgumentException("La categoría es obligatoria");
            }

            String skuLimpio = productoDto.getSku().trim();

            Producto existente = productoRepository.findBySkuIgnoreCase(skuLimpio);
            if (existente != null) {
                throw new IllegalArgumentException(
                        "El SKU '" + skuLimpio + "' ya existe. Está siendo usado por: " + existente.getNombreProducto());
            }

            Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría especificada no existe"));

            Producto producto = new Producto();
            producto.setSku(skuLimpio);
            producto.setNombreProducto(productoDto.getNombreProducto());
            producto.setDescripcion(productoDto.getDescripcion());
            producto.setStockActual(productoDto.getStockActual());
            producto.setPrecio(productoDto.getPrecio());
            producto.setTalla(productoDto.getTalla());
            producto.setColor(productoDto.getColor());
            producto.setEstado(productoDto.getEstado());
            producto.setImagenProducto(productoDto.getImagenProducto());
            producto.setCategoria(categoria);

            Producto guardado = productoRepository.save(producto);
            return mapToResponse(guardado);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error al guardar producto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error inesperado al guardar el producto", e);
        }
    }

    private ProductoResponseDto mapToResponse(Producto producto) {
        ProductoResponseDto dto = new ProductoResponseDto();
        dto.setIdProducto(producto.getIdProducto());
        dto.setSku(producto.getSku());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcion(producto.getDescripcion());
        dto.setStockActual(producto.getStockActual());
        dto.setPrecio(producto.getPrecio());
        dto.setTalla(producto.getTalla());
        dto.setColor(producto.getColor());
        dto.setEstado(producto.getEstado());
        dto.setImagenProducto(producto.getImagenProducto());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        dto.setCategoria(mapToCategoriaDto(producto.getCategoria()));
        return dto;
    }

    private CategoriaDto mapToCategoriaDto(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        CategoriaDto dto = new CategoriaDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombreCategoria(categoria.getNombreCategoria());
        dto.setDescripcion(categoria.getDescripcion());
        dto.setEstado(categoria.getEstado());
        dto.setFechaCreacion(categoria.getFechaCreacion());
        return dto;
    }
}
