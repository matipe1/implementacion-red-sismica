package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.CambioEstado;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("BLOQUEADO_EN_REVISION")
public class BloqueadoEnRevision extends Estado {

    public BloqueadoEnRevision(String nombre, String ambito) {
        super(null, nombre, ambito); // null para el id
    }

    @Override
    public void rechazar(Estado rechazado, LocalDateTime fechaHoraActual, Empleado responsableInspeccion, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {
        for (CambioEstado cambio : cambioEstados) {
            if (cambio.sosCEActual()){
                cambio.setFechaHoraHasta(fechaHoraActual);
            };
        }
        Estado rechazadoEstado = Rechazado.builder().nombre("Rechazado").ambito("Evento Sismico").build();

        CambioEstado rechazadoCambioEstado = CambioEstado.builder()
                .fechaHoraDesde(fechaHoraActual)
                .responsableInspeccion(responsableInspeccion)
                .estado(rechazado)
                .build();

        eventoSismico.agregarCambioDeEstado(rechazadoCambioEstado);
        eventoSismico.setEstadoActual(rechazadoEstado);
    }

}