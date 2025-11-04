package ar.edu.utn.dsi.ppai.entities.estados;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PENDIENTE_REVISION")
@NoArgsConstructor
public class PendienteRevision extends Estado {
    
}
