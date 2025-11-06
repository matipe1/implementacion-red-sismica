package ar.edu.utn.dsi.ppai.entities.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoSismicoDTO {
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaHoraOcurrencia;
    private BigDecimal latitudEpicentro;
    private BigDecimal longitudEpicentro;
    private BigDecimal latitudHipocentro;
    private BigDecimal longitudHipocentro;
    private BigDecimal valorMagnitud;
}
