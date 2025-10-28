package ar.edu.utn.dsi.ppai.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DetalleMuestraSismica {
    private String valor;
    private TipoDeDato tipoDeDato;

    public String[] getDatos() {
        String[] datos = new String[2];
        datos[0] = tipoDeDato.getDenominacion();
        datos[1] = valor;
        return datos;
    }
}
