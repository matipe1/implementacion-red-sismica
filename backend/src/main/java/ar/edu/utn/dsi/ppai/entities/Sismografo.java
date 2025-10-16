package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString // @ToString(exclude = {"estacionSismologica", "seriesTemporales"}) - Si hay relaciones bidireccionales
public class Sismografo {
    private Integer nroSerie;
    private Integer identificadorSismografo;
    private LocalDate fechaAdquisicion; 
    private EstacionSismologica estacionSismologica;
    private List<SerieTemporal> seriesTemporales;

    public Integer conocerCodigoEstacion() {
        return estacionSismologica.getCodigoEstacion();
    }

    public boolean contieneSerieTemporal(SerieTemporal serie) {
        return seriesTemporales.contains(serie);
    }
}
