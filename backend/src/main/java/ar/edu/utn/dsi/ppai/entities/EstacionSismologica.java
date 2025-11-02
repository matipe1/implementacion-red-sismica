package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estacion_sismologica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstacionSismologica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_estacion", nullable = false, unique = true)
    private Integer codigoEstacion;
}
