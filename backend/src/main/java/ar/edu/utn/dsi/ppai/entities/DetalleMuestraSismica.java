package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "detalle_muestra_sismica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleMuestraSismica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String valor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tipo_dato_denominacion", foreignKey = @ForeignKey(name = "fk_detalle_tipo_dato"))
    private TipoDeDato tipoDeDato;

    @ManyToOne(fetch = LAZY) @JoinColumn(name="muestra_sismica_id")
    private MuestraSismica muestraSismica;

    public String[] getDatos() {
        String[] datos = new String[2];
        datos[0] = tipoDeDato.getDenominacion();
        datos[1] = valor;
        return datos;
    }
}
