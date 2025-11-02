package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.EstacionSismologica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstacionSismologicaRepository extends JpaRepository<EstacionSismologica, Long> {
    Optional<EstacionSismologica> findByCodigoEstacion(Integer codigoEstacion);
}
