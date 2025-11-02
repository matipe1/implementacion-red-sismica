package ar.edu.utn.dsi.ppai.entities;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "cambio_estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_desde", nullable = false)
    private LocalDateTime fechaHoraDesde;

    @Column(name = "fecha_hora_hasta")
    private LocalDateTime fechaHoraHasta;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "responsable_inspeccion_id", foreignKey = @ForeignKey(name = "fk_cambio_estado_empleado"))
    private Empleado responsableInspeccion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "estado_id", foreignKey = @ForeignKey(name = "fk_cambio_estado_estado"))
    private Estado estado;

    @ManyToOne(fetch = LAZY) @JoinColumn(name="evento_sismico_id")
    private EventoSismico eventoSismico;

    public Boolean sosCEActual() {
        return this.fechaHoraHasta == null; // Si no tiene fecha final devuelve true
    }
}
