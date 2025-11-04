package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.CambioEstado;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("AUTODETECTADO")
@NoArgsConstructor
public class AutoDetectado extends Estado {

    public AutoDetectado(String nombre) {
        super(nombre);
    }

    @Override
    public void bloquear(LocalDateTime fechaHoraActual, List<CambioEstado> cambioEstados, EventoSismico eventoSismico, Estado estadoBloqueado) {
        for (CambioEstado cambio : cambioEstados) {
            if (cambio.sosCEActual()) {
                cambio.setFechaHoraHasta(fechaHoraActual);
            }
        }

        // Estado bloqueadoCambioEstado = new BloqueadoEnRevision("Bloqueado en revisión");

        CambioEstado bloqueadoCambioEstado = CambioEstado.builder()
                .fechaHoraDesde(fechaHoraActual)
                .estado(estadoBloqueado)
                .build();

        eventoSismico.agregarCambioDeEstado(bloqueadoCambioEstado);
        eventoSismico.setEstadoActual(estadoBloqueado);
    }
}
