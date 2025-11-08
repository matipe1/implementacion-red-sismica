package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alcance_sismo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlcanceSismo {

    @Id
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
