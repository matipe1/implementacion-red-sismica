package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "serie_temporal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SerieTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_inicio_registro", nullable = false)
    private LocalDateTime fechaHoraInicioRegistroMuestras;

    @Column(name = "frecuencia_muestreo", nullable = false, precision = 6, scale = 2)
    private BigDecimal frecuenciaMuestreo;

    @Column(name = "fecha_hora_registro", nullable = false)
    private LocalDateTime fechaHoraRegistro;

    @Column(name = "alerta_alarma", nullable = false)
    private Boolean alertaDeAlarma;

    @OneToMany(mappedBy = "serieTemporal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<MuestraSismica> muestrasSismicas = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "evento_sismico_id", foreignKey = @ForeignKey(name = "fk_serie_evento"))
    private EventoSismico eventoSismico;

    @ManyToOne(fetch = LAZY) @JoinColumn(name="sismografo_id")
    private Sismografo sismografo;

    public Integer buscarCodigoEstacionDeSismografo(List<Sismografo> sismografos) {
        for (Sismografo sismografo : sismografos) {
            for (SerieTemporal serie : sismografo.getSeriesTemporales()) {
                if (serie == this) {
                    return sismografo.conocerCodigoEstacion();
                }
            }
        }
        return null;
    }

    public List<MuestraSismica> obtenerMuestras() {
        return muestrasSismicas;
    }
}
