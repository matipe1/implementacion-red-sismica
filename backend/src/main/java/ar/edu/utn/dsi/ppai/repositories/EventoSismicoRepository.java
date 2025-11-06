package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;

public interface EventoSismicoRepository extends JpaRepository<EventoSismico, Long> {
    List<EventoSismico> findByEstadoActual(Estado estadoActual);

    @Query("""
      SELECT e FROM EventoSismico e
      WHERE e.fechaHoraOcurrencia = :fechaHoraOcurrencia
        AND e.latitudEpicentro = :latEpi
        AND e.longitudEpicentro = :lonEpi
        AND e.latitudHipocentro = :latHip
        AND e.longitudHipocentro = :lonHip
        AND e.valorMagnitud = :valor

    """)
    Optional<EventoSismico> findByDatosBase(
      @Param("fechaHoraOcurrencia") LocalDateTime fechaHoraOcurrencia,
      @Param("latEpi") BigDecimal latEpi,
      @Param("lonEpi") BigDecimal lonEpi,
      @Param("latHip") BigDecimal latHip,
      @Param("lonHip") BigDecimal lonHip,
      @Param("valor") BigDecimal valor
    );
}
