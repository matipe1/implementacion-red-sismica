package ar.edu.utn.dsi.ppai.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClasificacionSismo {
    private String nombre;
    private Double kmProfundidadDesde;
    private Double kmProfundidadHasta;
}
