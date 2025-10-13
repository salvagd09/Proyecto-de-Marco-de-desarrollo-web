package com.marcosdeDesarrollo.Ropa.Domain.Repository;

import com.marcosdeDesarrollo.Ropa.Persistencia.Entity.Auditoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

    Optional<Auditoria> findTopByTablaAfectadaAndIdRegistroAndTipoOperacionOrderByFechaDesc(
            String tablaAfectada,
            Integer idRegistro,
            String tipoOperacion);

    Optional<Auditoria> findTopByTablaAfectadaAndIdRegistroOrderByFechaDesc(String tablaAfectada, Integer idRegistro);
}
