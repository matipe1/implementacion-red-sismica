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
public class DetalleMuestraSismicaDTO {
    private String valor;

    private TipoDeDatoDTO tipoDeDato;
}
