package com.marcosdeDesarrollo.demo.Repository;

import com.marcosdeDesarrollo.demo.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol,Integer> {
}
