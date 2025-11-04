package ar.edu.utn.dsi.ppai.services.interfaces;

import java.util.List;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;

public interface IServicioRegistroRevision {
    public List<EventoSismicoDTO> opcionRegistrarRevisionManual();
    public EventoSismicoDetalleDTO tomarSeleccionDeEvento(Long eventoId);
    public void tomarRechazoDeEvento(Long eventoId);
}
