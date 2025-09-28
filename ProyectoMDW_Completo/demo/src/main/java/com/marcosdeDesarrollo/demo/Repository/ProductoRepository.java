package com.marcosdeDesarrollo.demo.Repository;

import com.marcosdeDesarrollo.demo.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
