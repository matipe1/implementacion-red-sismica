package ar.edu.utn.dsi.ppai.entities;

import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Estado {
    private String nombre;
    private String ambito;

    public boolean esAutoDetectado() {
        return (Objects.equals(nombre, "Autodetectado"));
    }

    public boolean esAmbitoEventoSismico() {
        return (Objects.equals(ambito, "Evento Sismico"));
    }

    public boolean esBloqueadoEnRevision() {
        return (Objects.equals(nombre, "Bloqueado en revision"));
    }

    public boolean esRechazado() {
        return (Objects.equals(nombre, "Rechazado"));
    }
}
