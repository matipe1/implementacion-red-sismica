package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MuestraSismica {
    private LocalDateTime fechaHoraMuestra;
    private List<DetalleMuestraSismica> detallesMuestrasSismicas;

    public List<DetalleMuestraSismica> obtenerDetallesMuestra() {
        return detallesMuestrasSismicas;
    }

    public List<String[]> getDatos() {
        List<String[]> datos = new ArrayList<>();
        for (DetalleMuestraSismica detalle : detallesMuestrasSismicas) {
            datos.add(detalle.getDatos());
        }
        return datos;
    }
}
