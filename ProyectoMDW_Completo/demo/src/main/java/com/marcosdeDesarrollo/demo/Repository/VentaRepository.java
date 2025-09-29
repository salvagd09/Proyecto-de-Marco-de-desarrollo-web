
package com.marcosdeDesarrollo.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.marcosdeDesarrollo.demo.Entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
}

