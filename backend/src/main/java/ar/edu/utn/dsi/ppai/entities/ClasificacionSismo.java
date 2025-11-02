package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clasificacion_sismo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClasificacionSismo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "km_profundidad_desde", nullable = false)
    private Double kmProfundidadDesde;

    @Column(name = "km_profundidad_hasta", nullable = false)
    private Double kmProfundidadHasta;
}
