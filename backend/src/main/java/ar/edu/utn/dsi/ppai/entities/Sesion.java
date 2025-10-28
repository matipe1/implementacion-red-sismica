package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Sesion {
    private LocalDateTime fechaHora;
    private Usuario usuario;

    public Empleado buscarASLogueado() {
        return usuario.getASLogueado();
    }
}
