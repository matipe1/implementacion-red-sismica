package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
