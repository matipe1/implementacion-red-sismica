package ar.edu.utn.dsi.ppai.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_de_dato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDeDato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String denominacion;

    @Column(name = "nombre_unidad_medida", nullable = false, length = 100)
    private String nombreUnidadMedida;

    @Column(name = "valor_umbral", nullable = false, precision = 4, scale = 2)
    private BigDecimal valorUmbral;
}
