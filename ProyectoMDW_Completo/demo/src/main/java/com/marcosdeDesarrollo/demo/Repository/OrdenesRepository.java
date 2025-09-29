package com.marcosdeDesarrollo.demo.Repository;

import com.marcosdeDesarrollo.demo.Entity.Gastos;
import com.marcosdeDesarrollo.demo.Entity.Ordenes_Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenesRepository extends JpaRepository<Ordenes_Compra, Integer> {
}
