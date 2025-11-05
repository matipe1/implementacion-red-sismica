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
public class CoordenadasAproximadasDTO {
    private Double latitudEpicentro;
    private Double longitudEpicentro;
    private Double latitudHipocentro;
    private Double longitudHipocentro;
}
