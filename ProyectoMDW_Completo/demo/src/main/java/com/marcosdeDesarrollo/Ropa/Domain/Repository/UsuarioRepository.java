package com.marcosdeDesarrollo.Ropa.Domain.Repository;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    // AÑADE ESTA LÍNEA, ES PARA VERIFICAR SI UN EMAIL YA EXISTE
    Boolean existsByEmail(String email);

    long countByRol_Id(Integer idRol);

    List<Usuario> findByRol_Id(Integer idRol);

    long countByRol_IdAndEstado(Integer idRol, String estado);
}
