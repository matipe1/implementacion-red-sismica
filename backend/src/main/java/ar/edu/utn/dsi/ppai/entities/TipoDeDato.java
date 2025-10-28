package ar.edu.utn.dsi.ppai.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TipoDeDato {
    private String denominacion;
    private String nombreUnidadMedida;
    private Integer valor;
}
