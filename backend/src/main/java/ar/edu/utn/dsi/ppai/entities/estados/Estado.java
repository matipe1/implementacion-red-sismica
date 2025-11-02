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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_estado", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String ambito;

    public boolean esAutoDetectado() {
        return Objects.equals(nombre, "Autodetectado");
    }

    public boolean esAmbitoEventoSismico() {
        return Objects.equals(ambito, "Evento Sismico");
    }

    public boolean esBloqueadoEnRevision() {
        return Objects.equals(nombre, "Bloqueado en revision");
    }

    public boolean esRechazado() {
        return Objects.equals(nombre, "Rechazado");
    }

    public void rechazar(LocalDateTime fechaHoraActual, Empleado responsableInspeccion, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {

    }

    public void bloquear(LocalDateTime fechaHoraActual, Empleado responsableInspeccion, List<CambioEstado> cambioEstados, EventoSismico eventoSismico) {

    }
}
