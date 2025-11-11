package ar.edu.utn.dsi.ppai.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoSismicoId implements Serializable{
    
    @Column(name = "latitud_epicentro", precision = 9, scale = 6)
    private BigDecimal latitudEpicentro;

    @Column(name = "longitud_epicentro", precision = 9, scale = 6)
    private BigDecimal longitudEpicentro;

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventoSismicoId that)) return false;
        return Objects.equals(latitudEpicentro, that.latitudEpicentro)
                && Objects.equals(longitudEpicentro, that.longitudEpicentro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitudEpicentro, longitudEpicentro);
    }
}
