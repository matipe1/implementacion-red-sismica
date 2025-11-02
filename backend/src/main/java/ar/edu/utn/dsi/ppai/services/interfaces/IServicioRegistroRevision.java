package ar.edu.utn.dsi.ppai.services.interfaces;

import java.util.List;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;

public interface IServicioRegistroRevision {
    List<EventoSismicoDTO> buscarEventosParaRevision();
}
