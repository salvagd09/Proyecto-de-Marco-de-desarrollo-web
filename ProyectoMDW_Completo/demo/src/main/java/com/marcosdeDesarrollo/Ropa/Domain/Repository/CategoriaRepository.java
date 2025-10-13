package com.marcosdeDesarrollo.Ropa.Domain.Repository;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}