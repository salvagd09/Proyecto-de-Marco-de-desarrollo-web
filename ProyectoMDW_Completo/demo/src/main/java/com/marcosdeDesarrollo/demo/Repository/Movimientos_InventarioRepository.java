package com.marcosdeDesarrollo.demo.Repository;

import com.marcosdeDesarrollo.demo.Entity.Insumos;
import com.marcosdeDesarrollo.demo.Entity.Movimientos_inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Movimientos_InventarioRepository extends JpaRepository<Movimientos_inventario,Integer> {
}
