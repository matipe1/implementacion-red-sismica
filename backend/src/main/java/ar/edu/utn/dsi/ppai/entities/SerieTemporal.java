package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SerieTemporal {
    private LocalDateTime fechaHoraInicioRegistroMuestras;
    private String frecuenciaMuestreo;
    private LocalDateTime fechaHoraRegistro;
    private Boolean alertaDeAlarma;
    private List<MuestraSismica> muestrasSismicas;

    public List<MuestraSismica> obtenerMuestras() {
        return muestrasSismicas;
    }

    public Integer buscarCodigoEstacionDeSismografo(List<Sismografo> allSismografos) {
        for (Sismografo sismografo : allSismografos) {
            for (SerieTemporal serie : sismografo.getSeriesTemporales()) {
                if (serie == this) { // Comparacion de punteros
                    return sismografo.conocerCodigoEstacion();
                }
            }
        }
        return null;
    }

}
