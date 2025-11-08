package ar.edu.utn.dsi.ppai.entities.estados;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pendiente_revision")
@NoArgsConstructor
public class PendienteRevision extends Estado {
    
}
