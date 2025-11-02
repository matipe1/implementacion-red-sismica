package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Optional<Estado> findByNombre(String nombre);
}
