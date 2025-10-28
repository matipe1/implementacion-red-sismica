package ar.edu.utn.dsi.ppai.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Empleado {
    private String nombre;
    private String apellido;
    private String mail;
    private String telefono;
}
