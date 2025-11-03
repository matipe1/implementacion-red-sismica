package ar.edu.utn.dsi.ppai.services.interfaces;

import java.util.List;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;

public interface IServicioRegistroRevision {
    List<EventoSismicoDTO> opcionRegistrarRevisionManual();
    EventoSismicoDetalleDTO tomarSeleccionDeEvento(Long eventoId);
}
