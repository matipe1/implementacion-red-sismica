package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "origen_de_generacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrigenDeGeneracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
