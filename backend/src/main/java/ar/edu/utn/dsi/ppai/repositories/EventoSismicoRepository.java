package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.stream.Stream;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;

public interface EventoSismicoRepository extends JpaRepository<EventoSismico, Long> {
    Stream<EventoSismico> findByEstadoActual(Estado estadoActual);
}

