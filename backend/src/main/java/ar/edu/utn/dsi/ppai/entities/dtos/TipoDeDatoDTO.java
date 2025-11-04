package ar.edu.utn.dsi.ppai.entities.dtos;

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
public class TipoDeDatoDTO {
    private String denominacion;
    private String nombreUnidadMedida;
    private Integer valorUmbral;
}
