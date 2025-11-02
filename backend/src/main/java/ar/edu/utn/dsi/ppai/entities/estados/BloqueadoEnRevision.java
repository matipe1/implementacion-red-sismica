package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.Empleado;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("BLOQUEADO_EN_REVISION")
public class BloqueadoEnRevision extends Estado {

    @Override
    public void rechazar(Estado rechazado, LocalDateTime fechaHoraActual, Empleado responsableInspeccion) {

    }

}