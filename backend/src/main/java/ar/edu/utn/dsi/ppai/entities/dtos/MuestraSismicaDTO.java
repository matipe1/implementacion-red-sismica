package ar.edu.utn.dsi.ppai.entities.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuestraSismicaDTO {
    private LocalDateTime fechaHoraMuestra;

    private List<DetalleMuestraSismicaDTO> detalles;
}
