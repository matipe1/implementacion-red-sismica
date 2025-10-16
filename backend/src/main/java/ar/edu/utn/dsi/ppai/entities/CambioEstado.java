package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString // @ToString(exclude = {"responsableInspeccion", "estado"}) - Si hay relaciones bidireccionales
public class CambioEstado {
    private LocalDateTime fechaHoraDesde;
    private LocalDateTime fechaHoraHasta;
    private Empleado responsableInspeccion;
    private Estado estado;

    public Boolean sosCEActual() { 
        return this.fechaHoraHasta == null; 
    }
}
