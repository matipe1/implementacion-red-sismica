package ar.edu.utn.dsi.ppai.entities.estados;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("RECHAZADO")
@NoArgsConstructor
public class Rechazado extends Estado{
    public Rechazado(String nombre) {
        super(nombre);
    }
}
