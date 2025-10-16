package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString // @ToString(exclude = {"usuario"}) - Si hay relación bidireccional
public class Sesion {
    private LocalDateTime fechaHora;
    private Usuario usuario;

    public Empleado buscarASLogueado() {
        return usuario.getASLogueado();
    }
}
