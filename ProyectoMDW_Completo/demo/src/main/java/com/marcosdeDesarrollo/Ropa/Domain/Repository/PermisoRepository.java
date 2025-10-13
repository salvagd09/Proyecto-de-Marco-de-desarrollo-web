package com.marcosdeDesarrollo.Ropa.Domain.Repository;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
}
