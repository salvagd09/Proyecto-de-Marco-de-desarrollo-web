package com.marcosdeDesarrollo.demo.Repository;


import com.marcosdeDesarrollo.demo.Entity.Tipos_Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TiposGastoRepository extends JpaRepository<Tipos_Gasto, Integer> {
}
