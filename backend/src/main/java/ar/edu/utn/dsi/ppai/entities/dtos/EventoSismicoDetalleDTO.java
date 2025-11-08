package ar.edu.utn.dsi.ppai.entities.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    private EventoSismicoDTO eventoSismico;
    private DatosSismicosDTO datosSismicos;
    private List<SerieTemporalDTO> seriesTemporales;

    private List<CambioEstadoDTO> cambiosDeEstado; // prueba
}
