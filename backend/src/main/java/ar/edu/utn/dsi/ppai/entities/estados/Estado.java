package ar.edu.utn.dsi.ppai.entities.estados;

import ar.edu.utn.dsi.ppai.entities.CambioEstado;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "estado")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@NoArgsConstructor
public abstract class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_seq")
    @SequenceGenerator(name = "estado_seq", sequenceName = "estado_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nombre", unique = true, nullable = false, length = 100)
    private String nombre;

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    public void autoconfirmar() {
        throw new UnsupportedOperationException(
            "El evento no se puede autoconfirmar desde este estado."
        );
    }

    public void autodetectar() {
        throw new UnsupportedOperationException(
            "El evento no se puede autodetectar desde este estado."
        );
    }

    public void derivarASupervisor() {
        throw new UnsupportedOperationException(
            "El evento no se puede derivar a supervisor desde este estado."
        );
    }

    public void confirmar() {
        throw new UnsupportedOperationException(
            "El evento no se puede confirmar desde este estado."
        );
    }

    public void marcarSinRevision() {
        throw new UnsupportedOperationException(
            "El evento no se puede marcar sin revisión desde este estado."
        );
    }

    public void marcarPendienteDeCierre() {
        throw new UnsupportedOperationException(
            "El evento no se puede marcar pendiente de cierre desde este estado."
        );
    }

    public void marcarPendienteDeRevision() {
        throw new UnsupportedOperationException(
            "El evento no se puede marcar pendiente de revisión desde este estado."
        );
    }

    public void cerrar() {
        throw new UnsupportedOperationException(
            "El evento no se puede cerrar desde este estado."
        );
    }

    public void bloquear(LocalDateTime fechaHoraActual, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {
        throw new UnsupportedOperationException(
            "No se puede bloquear el evento en este estado."
        );
    }

    public void rechazar(LocalDateTime fechaHoraActual, Empleado responsableInspeccion, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {
        throw new UnsupportedOperationException(
            "No se puede rechazar el evento en este estado."
        );
    }

    // equals y hashCode basados únicamente en nombre
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Estado)) return false;
        Estado e = (Estado) obj;
        return Objects.equals(nombre, e.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
