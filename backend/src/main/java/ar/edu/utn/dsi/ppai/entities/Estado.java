package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estado {

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
}
