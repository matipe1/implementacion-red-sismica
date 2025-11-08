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
    @Column(name = "codigo_estacion", nullable = false, unique = true)
    private Integer codigoEstacion;
}
