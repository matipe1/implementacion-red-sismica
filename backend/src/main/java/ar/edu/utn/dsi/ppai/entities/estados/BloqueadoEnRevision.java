package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.CambioEstado;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bloqueado_en_revision")
@NoArgsConstructor
public class BloqueadoEnRevision extends Estado {

    public BloqueadoEnRevision(String nombre) {
        super(nombre);
    }

    @Override
    public void rechazar(LocalDateTime fechaHoraActual, Empleado responsableInspeccion, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {
        for (CambioEstado cambio : cambioEstados) {
            if (cambio.sosCEActual()) {
                cambio.setFechaHoraHasta(fechaHoraActual);
            }
        }
        
        Estado estadoRechazado = new Rechazado("Rechazado");

        CambioEstado rechazadoCambioEstado = CambioEstado.builder()
                .fechaHoraDesde(fechaHoraActual)
                .responsableInspeccion(responsableInspeccion)
                .estado(estadoRechazado)
                .build();

        eventoSismico.agregarCambioDeEstado(rechazadoCambioEstado);
        eventoSismico.setEstadoActual(estadoRechazado);
    }

}