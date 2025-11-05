package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;

public interface EventoSismicoRepository extends JpaRepository<EventoSismico, Long> {
    List<EventoSismico> findByEstadoActual(Estado estadoActual);

    @Query("""
      SELECT e FROM EventoSismico e
      WHERE e.latitudEpicentro BETWEEN :latEpi - 0.5 AND :latEpi + 0.5
        AND e.longitudEpicentro BETWEEN :lonEpi - 0.5 AND :lonEpi + 0.5
        AND e.latitudHipocentro BETWEEN :latHip - 0.5 AND :latHip + 0.5
        AND e.longitudHipocentro BETWEEN :lonHip - 0.5 AND :lonHip + 0.5
    """)
    Optional<EventoSismico> findByCoordenadasAproximadas(
      @Param("latEpi") Double latEpi,
      @Param("lonEpi") Double lonEpi,
      @Param("latHip") Double latHip,
      @Param("lonHip") Double lonHip
    );
}
