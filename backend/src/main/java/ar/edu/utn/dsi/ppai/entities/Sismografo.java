package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sismografo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sismografo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_serie", nullable = false, unique = true)
    private Integer nroSerie;

    @Column(name = "identificador_sismografo", nullable = false, unique = true)
    private Integer identificadorSismografo;

    @Column(name = "fecha_adquisicion", nullable = false)
    private LocalDate fechaAdquisicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacion_sismologica_id", foreignKey = @ForeignKey(name = "fk_sismografo_estacion"))
    private EstacionSismologica estacionSismologica;

    @OneToMany(mappedBy = "sismografo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SerieTemporal> seriesTemporales = new ArrayList<>();

    public Integer conocerCodigoEstacion() {
        return estacionSismologica.getCodigoEstacion();
    }

    public boolean contieneSerieTemporal(SerieTemporal serie) {
        return seriesTemporales.contains(serie);
    }
}
