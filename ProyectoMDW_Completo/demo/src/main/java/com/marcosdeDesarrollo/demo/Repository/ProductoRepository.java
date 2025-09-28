package com.marcosdeDesarrollo.demo.Repository;

import com.marcosdeDesarrollo.demo.Entity.Estado;
import com.marcosdeDesarrollo.demo.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
     // Método para contar productos activos
    long countByEstado(Estado estado);

    // Método para contar productos con stock bajo (menor a 20)
    long countByStockActualLessThan(int stock);
}
