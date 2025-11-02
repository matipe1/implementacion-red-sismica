package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "muestra_sismica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MuestraSismica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_muestra", nullable = false)
    private LocalDateTime fechaHoraMuestra;

    @OneToMany(mappedBy = "muestraSismica", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleMuestraSismica> detallesMuestrasSismicas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_serie_temporal")  // o el nombre real de tu FK en la base
    private SerieTemporal serieTemporal;

    public List<DetalleMuestraSismica> obtenerDetallesMuestra() {
        return detallesMuestrasSismicas;
    }

    public List<String[]> getDatos() {
        List<String[]> datos = new ArrayList<>();
        for (DetalleMuestraSismica detalle : detallesMuestrasSismicas) {
            datos.add(detalle.getDatos());
        }
        return datos;
    }
}
