package ar.edu.utn.dsi.ppai.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario {
    private String nombreUsuario;
    private String contraseña;
    private Empleado empleado;

    public Empleado getASLogueado() {
        return empleado;
    }
}
