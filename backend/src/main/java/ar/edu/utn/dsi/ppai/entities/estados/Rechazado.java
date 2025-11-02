package ar.edu.utn.dsi.ppai.entities.estados;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RECHAZADO")
public class Rechazado extends Estado{
    public Rechazado(String nombre) {
        super(nombre);
    }
}
