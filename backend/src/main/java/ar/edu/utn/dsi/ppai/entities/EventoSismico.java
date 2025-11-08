package ar.edu.utn.dsi.ppai.entities;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "evento_sismico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoSismico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_ocurrencia", nullable = false)
    private LocalDateTime fechaHoraOcurrencia;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Column(name = "latitud_epicentro", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitudEpicentro;

    @Column(name = "longitud_epicentro", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitudEpicentro;

    @Column(name = "latitud_hipocentro", precision = 9, scale = 6)
    private BigDecimal latitudHipocentro;

    @Column(name = "longitud_hipocentro", precision = 9, scale = 6)
    private BigDecimal longitudHipocentro;

    @Column(name = "valor_magnitud", nullable = false, precision = 4, scale = 2)
    private BigDecimal valorMagnitud;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clasificacion_sismo_id", foreignKey = @ForeignKey(name = "fk_evento_clasificacion"))
    private ClasificacionSismo clasificacionSismo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_generacion_id", foreignKey = @ForeignKey(name = "fk_evento_origen"))
    private OrigenDeGeneracion origenDeGeneracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcance_sismo_id", foreignKey = @ForeignKey(name = "fk_evento_alcance"))
    private AlcanceSismo alcanceSismo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analista_supervisor_mail", foreignKey = @ForeignKey(name = "fk_evento_empleado"))
    private Empleado analistaSupervisor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_actual_id", foreignKey = @ForeignKey(name = "fk_evento_estado"))
    private Estado estadoActual;
    
    @OneToMany(mappedBy = "eventoSismico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CambioEstado> cambiosDeEstado = new ArrayList<>();

    @OneToMany(mappedBy = "eventoSismico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SerieTemporal> seriesTemporales = new ArrayList<>();

    public String getFechaHoraOcurrenciaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaHoraOcurrencia.format(formatter);
    }
    
    public Map<Integer, List<SerieTemporal>> obtenerSeriesTemporalesClasificadas(List<Sismografo> sismografos) {
        Map<Integer, List<SerieTemporal>> seriesClasificadas = new HashMap<>();
        
        for (SerieTemporal serie : seriesTemporales) {
            Integer codigoEstacion = serie.buscarCodigoEstacionDeSismografo(sismografos);
            seriesClasificadas.computeIfAbsent(codigoEstacion, k -> new ArrayList<>())
            .add(serie); // si no hay codigoEstacion
        }
        return seriesClasificadas;
    };

    public void agregarCambioDeEstado(CambioEstado nuevoCambio) {
        nuevoCambio.setEventoSismico(this);
        if (!cambiosDeEstado.contains(nuevoCambio)) {
            cambiosDeEstado.add(nuevoCambio);
        }
    }

    // Propios del patron State
    public void bloquear(LocalDateTime fechaHoraActual) {
        estadoActual.bloquear(fechaHoraActual, cambiosDeEstado, this);
    }

    public void rechazar(LocalDateTime fechaHoraActual, Empleado responsableInspeccion) {
        estadoActual.rechazar(fechaHoraActual, responsableInspeccion, cambiosDeEstado, this);
    }
}
