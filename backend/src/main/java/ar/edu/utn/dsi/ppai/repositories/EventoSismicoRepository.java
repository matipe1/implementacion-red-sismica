package ar.edu.utn.dsi.ppai.repositories;

import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import ar.edu.utn.dsi.ppai.entities.estados.Estado;

public interface EventoSismicoRepository extends JpaRepository<EventoSismico, Long> {
    List<EventoSismico> findByEstadoActual(Estado estadoActual);
}

