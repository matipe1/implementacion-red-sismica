package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.Sismografo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SismografoRepository extends JpaRepository<Sismografo, Long> {
    Optional<Sismografo> findByNroSerie(Integer nroSerie);
    Optional<Sismografo> findByIdentificadorSismografo(Integer identificadorSismografo);
}
