package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.CambioEstado;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("AUTODETECTADO")
public class AutoDetectado extends Estado{

    @Override
    public void bloquear(LocalDateTime fechaHoraActual, Empleado responsableInspeccion, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {
        for (CambioEstado cambio : cambioEstados) {
            if (cambio.sosCEActual()){
                cambio.setFechaHoraHasta(fechaHoraActual);
            };
        }
        Estado bloqueadoEstado = BloqueadoEnRevision.builder().nombre("Bloqueado en Revision").ambito("Evento Sismico").build();

        CambioEstado bloqueadoCambioEstado = CambioEstado.builder()
                .fechaHoraDesde(fechaHoraActual)
                .responsableInspeccion(responsableInspeccion)
                .estado(bloqueadoEstado)
                .build();

        eventoSismico.agregarCambioDeEstado(bloqueadoCambioEstado);
        eventoSismico.setEstadoActual(bloqueadoEstado);
    }
}
