package ar.edu.utn.dsi.ppai.entities.dtos;

import java.time.LocalDateTime;

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
public class CambioEstadoDTO {
    LocalDateTime fechaHoraDesde;
    LocalDateTime fechaHoraHasta;
    String responsableInspeccion;
    String estado;
}
