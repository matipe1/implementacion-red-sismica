package ar.edu.utn.dsi.ppai.entities.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoSismicoDetalleDTO {
    private Long id;
    private String fechaHoraOcurrencia;
    private String ubicacion;
    private Double valorMagnitud;

    private String alcancesSismo;
    private String clasificacionSismo;
    private String origenDeGeneracion;

    private List<SerieTemporalDTO> seriesTemporales; // ver bien que informacion buscamos de las series y como
}
