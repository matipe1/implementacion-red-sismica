package ar.edu.utn.dsi.ppai.entities.estados;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rechazado")
@NoArgsConstructor
public class Rechazado extends Estado{
    public Rechazado(String nombre) {
        super(nombre);
    }
}
