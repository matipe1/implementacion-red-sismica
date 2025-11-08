package ar.edu.utn.dsi.ppai.services.interfaces;

import java.util.List;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;

public interface IGestorRegistroRevision {
    public List<EventoSismicoDTO> opcionRegistrarRevisionManual();
    public EventoSismicoDetalleDTO tomarSeleccionDeEvento(EventoSismicoDTO eventoDTO);
    public void tomarRechazoDeEvento(EventoSismicoDTO eventoDTO);
}
