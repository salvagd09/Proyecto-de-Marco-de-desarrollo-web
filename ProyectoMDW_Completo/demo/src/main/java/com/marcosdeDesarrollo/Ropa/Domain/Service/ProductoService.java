package com.marcosdeDesarrollo.Ropa.Domain.Service;

import com.marcosdeDesarrollo.Ropa.Domain.DTO.CategoriaDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.ProductoRequestDto;
import com.marcosdeDesarrollo.Ropa.Domain.DTO.ProductoResponseDto;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Categoria;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Estado;
import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Producto;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.CategoriaRepository;
import com.marcosdeDesarrollo.Ropa.Domain.Repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductoService {

    private static final int STOCK_UMBRAL_BAJO = 20;
    private static final BigDecimal PRECIO_ECONOMICO_MAX = BigDecimal.valueOf(50);
    private static final BigDecimal PRECIO_MEDIO_MIN = BigDecimal.valueOf(51);
    private static final BigDecimal PRECIO_MEDIO_MAX = BigDecimal.valueOf(120);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<ProductoResponseDto> obtenerTodosLosProductos() {
        return obtenerProductosFiltrados(null, null, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDto> obtenerProductosFiltrados(String estado,
            String stock,
            String precio,
            Long categoriaId,
            String search) {

        Specification<Producto> spec = construirSpecification(estado, stock, precio, categoriaId, search);

        return productoRepository.findAll(spec)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public long contarProductosActivos() {
        return productoRepository.countByEstado(Estado.Activo);
    }

    public long contarProductosInactivos() {
        return productoRepository.countByEstado(Estado.Inactivo);
    }

    public long contarStockBajo() {
        return productoRepository.countByStockActualLessThan(STOCK_UMBRAL_BAJO);
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

            if (productoRepository.existsBySkuIgnoreCase(skuLimpio)) {
                throw new IllegalArgumentException(
                        "El SKU '" + skuLimpio + "' ya existe. Por favor ingresa uno diferente.");
            }

            Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría especificada no existe"));

            Producto producto = new Producto();
            producto.setSku(skuLimpio);
            aplicarDatosProducto(producto, productoDto, categoria);
            if (producto.getEstado() == null) {
                producto.setEstado(Estado.Activo);
            }

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

    public ProductoResponseDto actualizarProducto(Long idProducto, ProductoRequestDto productoDto) {
        try {
            if (productoDto == null) {
                throw new IllegalArgumentException("Los datos del producto son obligatorios");
            }

            Producto productoExistente = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("El producto indicado no existe"));

            if (productoDto.getSku() == null || productoDto.getSku().trim().isEmpty()) {
                throw new IllegalArgumentException("El SKU es obligatorio");
            }

            String skuLimpio = productoDto.getSku().trim();
            if (productoRepository.existsBySkuIgnoreCaseAndIdProductoNot(skuLimpio, idProducto)) {
                throw new IllegalArgumentException("El SKU '" + skuLimpio + "' ya está asignado a otro producto");
            }

            if (productoDto.getCategoriaId() == null || productoDto.getCategoriaId() <= 0) {
                throw new IllegalArgumentException("La categoría es obligatoria");
            }

            Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría especificada no existe"));

            productoExistente.setSku(skuLimpio);
            aplicarDatosProducto(productoExistente, productoDto, categoria);

            Producto actualizado = productoRepository.save(productoExistente);
            return mapToResponse(actualizado);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error al actualizar producto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("❌ Error inesperado al actualizar: " + e.getMessage());
            throw new RuntimeException("Error inesperado al actualizar el producto", e);
        }
    }

    public ProductoResponseDto actualizarEstadoProducto(Long idProducto, Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto indicado no existe"));

        producto.setEstado(estado);
        Producto actualizado = productoRepository.save(producto);
        return mapToResponse(actualizado);
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

    private void aplicarDatosProducto(Producto producto, ProductoRequestDto productoDto, Categoria categoria) {
        producto.setNombreProducto(productoDto.getNombreProducto());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setStockActual(productoDto.getStockActual());
        producto.setPrecio(productoDto.getPrecio());
        producto.setTalla(productoDto.getTalla());
        producto.setColor(productoDto.getColor());
        producto.setEstado(productoDto.getEstado());
        producto.setImagenProducto(productoDto.getImagenProducto());
        producto.setCategoria(categoria);
    }

    @SuppressWarnings("removal")
    private Specification<Producto> construirSpecification(String estado,
            String stock,
            String precio,
            Long categoriaId,
            String search) {
        Specification<Producto> spec = Specification.where(null);

        if (estado != null && !estado.isBlank()) {
            Estado estadoEnum = parseEstado(estado);
            spec = spec.and((root, query, cb) -> cb.equal(root.get("estado"), estadoEnum));
        }

        if (stock != null && !stock.isBlank()) {
            String stockLower = stock.toLowerCase(Locale.ROOT);
            switch (stockLower) {
                case "bajo" ->
                    spec = spec.and((root, query, cb) -> cb.lessThan(root.get("stockActual"), STOCK_UMBRAL_BAJO));
                case "alto" -> spec = spec
                        .and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("stockActual"), STOCK_UMBRAL_BAJO));
                default -> {
                }
            }
        }

        if (precio != null && !precio.isBlank()) {
            String precioLower = precio.toLowerCase(Locale.ROOT);
            switch (precioLower) {
                case "economico" -> spec = spec
                        .and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("precio"), PRECIO_ECONOMICO_MAX));
                case "medio" -> spec = spec
                        .and((root, query, cb) -> cb.between(root.get("precio"), PRECIO_MEDIO_MIN, PRECIO_MEDIO_MAX));
                case "premium" ->
                    spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("precio"), PRECIO_MEDIO_MAX));
                default -> {
                }
            }
        }

        if (categoriaId != null && categoriaId > 0) {
            spec = spec.and((root, query, cb) -> cb.equal(root.join("categoria").get("idCategoria"), categoriaId));
        }

        if (search != null && !search.isBlank()) {
            String criterio = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("nombreProducto")), criterio),
                    cb.like(cb.lower(root.get("sku")), criterio)));
        }

        return spec;
    }

    private Estado parseEstado(String estado) {
        String valor = estado.trim().toLowerCase(Locale.ROOT);
        try {
            return switch (valor) {
                case "activo" -> Estado.Activo;
                case "inactivo" -> Estado.Inactivo;
                default -> Estado.valueOf(estado.trim());
            };
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
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
