package com.marcosdeDesarrollo.demo.Repository;
import com.marcosdeDesarrollo.demo.Entity.Estado;
import com.marcosdeDesarrollo.demo.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    // Métodos para SKU
    boolean existsBySku(String sku);

    Producto findBySkuIgnoreCase(String sku);

    // Método para contar productos activos
    long countByEstado(Estado estado);

    boolean existsBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCaseAndIdProductoNot(String sku, Long idProducto);

    long countByStockActualLessThan(int stock);

}
