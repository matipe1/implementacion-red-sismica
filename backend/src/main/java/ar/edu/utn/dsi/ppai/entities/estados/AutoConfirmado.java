package ar.edu.utn.dsi.ppai.entities.estados;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "autoconfirmado")
@NoArgsConstructor
public class AutoConfirmado extends Estado {
    
}
