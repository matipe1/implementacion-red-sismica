package ar.edu.utn.dsi.ppai.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contraseña;

    @OneToOne
    @JoinColumn(name = "empleado_mail", foreignKey = @ForeignKey(name = "fk_usuario_empleado"))
    private Empleado empleado;

    public Empleado getASLogueado() {
        return empleado;
    }
}
