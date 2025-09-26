package com.marcosdeDesarrollo.demo.Repository;

import com.marcosdeDesarrollo.demo.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    // AÑADE ESTA LÍNEA, ES PARA VERIFICAR SI UN EMAIL YA EXISTE
    Boolean existsByEmail(String email);
}