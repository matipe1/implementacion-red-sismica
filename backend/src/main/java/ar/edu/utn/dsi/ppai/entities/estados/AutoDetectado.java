package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.CambioEstado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "autodetectado")
@NoArgsConstructor
public class AutoDetectado extends Estado {

    public AutoDetectado(String nombre) {
        super(nombre);
    }

    @Override
    public void bloquear(LocalDateTime fechaHoraActual, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {
        for (CambioEstado cambio : cambioEstados) {
            if (cambio.sosCEActual()) {
                cambio.setFechaHoraHasta(fechaHoraActual);
            }
        }

        Estado estadoBloqueadoEnRevision = new BloqueadoEnRevision("Bloqueado en revisión");

        CambioEstado bloqueadoCambioEstado = CambioEstado.builder()
                .fechaHoraDesde(fechaHoraActual)
                .estado(estadoBloqueadoEnRevision)
                .build();

        eventoSismico.agregarCambioDeEstado(bloqueadoCambioEstado);
        eventoSismico.setEstadoActual(estadoBloqueadoEnRevision);
    }
}
