package ar.edu.utn.dsi.ppai.entities;

import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import jakarta.persistence.*;
import lombok.*;
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
    private Long id;

    @Column(name = "fecha_hora_ocurrencia", nullable = false)
    private LocalDateTime fechaHoraOcurrencia;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Column(name = "latitud_epicentro", nullable = false)
    private Double latitudEpicentro;

    @Column(name = "longitud_epicentro", nullable = false)
    private Double longitudEpicentro;

    @Column(name = "latitud_hipocentro")
    private Double latitudHipocentro;

    @Column(name = "longitud_hipocentro")
    private Double longitudHipocentro;

    @Column(name = "valor_magnitud", nullable = false)
    private Double valorMagnitud;

    @OneToMany(mappedBy = "eventoSismico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SerieTemporal> seriesTemporales = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analista_supervisor_id", foreignKey = @ForeignKey(name = "fk_evento_empleado"))
    private Empleado analistaSupervisor;

    @OneToMany(mappedBy = "eventoSismico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CambioEstado> cambiosDeEstado = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_actual_id", foreignKey = @ForeignKey(name = "fk_evento_estado"))
    private Estado estadoActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clasificacion_sismo_id", foreignKey = @ForeignKey(name = "fk_evento_clasificacion"))
    private ClasificacionSismo clasificacionSismo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_generacion_id", foreignKey = @ForeignKey(name = "fk_evento_origen"))
    private OrigenDeGeneracion origenDeGeneracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcance_sismo_id", foreignKey = @ForeignKey(name = "fk_evento_alcance"))
    private AlcanceSismo alcanceSismo;

    public String getFechaHoraOcurrencia() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHoraOcurrencia.format(formatter);
    }

    public String getUbicacion() {
        return "Epicentro: (" + latitudEpicentro + "," + longitudEpicentro + ") - "
                + "Hipocentro: (" + latitudHipocentro + "," + longitudHipocentro + ")";
    }

    public void rechazar(LocalDateTime fechaHoraActual, Empleado responsableInspeccion) {
        estadoActual.rechazar(fechaHoraActual, responsableInspeccion, cambiosDeEstado, this);
        System.out.println("Estado seteado a 'RECHAZADO'");
    }

    public void bloquear(LocalDateTime fechaHoraActual, Empleado responsableInspeccion) {
        estadoActual.bloquear(fechaHoraActual, responsableInspeccion, cambiosDeEstado, this);
        System.out.println("Estado seteado a 'BLOQUEADO EN REVISION'");
    }

    public void agregarCambioDeEstado(CambioEstado nuevoCambio) { // cambiar por set y sacar el setter de lombok por defecto
        if (cambiosDeEstado == null) {
            cambiosDeEstado = new ArrayList<>();
        }
        System.out.println("Tamaño antes de agregar el ce: " + cambiosDeEstado.size());
        nuevoCambio.setEventoSismico(this); // para que persista con el JPA
        cambiosDeEstado.add(nuevoCambio);
        System.out.println("Tamaño despues de agregar el ce: " + cambiosDeEstado.size());
    }


    // METODO DISPARADOR DEL LOOP PARA BUSCAR LAS SERIES Y CLASIFICARLAS
    public Map<Integer, List<SerieTemporal>> obtenerSeriesTemporalesClasificadas(List<Sismografo> allSismografos) {
        Map<Integer, List<SerieTemporal>> clasificadas = new HashMap<>();

        for (SerieTemporal serie : seriesTemporales) {
            Integer codigoEstacion = serie.buscarCodigoEstacionDeSismografo(allSismografos);
            clasificadas.computeIfAbsent(codigoEstacion, k -> new ArrayList<>()).add(serie);
        }
        System.out.println("Series clasificadas: \n" + clasificadas);
        return clasificadas;
    };
}

/*
MÉTODOS PERTENECIENTES A LA MAQUINA DE ESTADOS Y EL DOMINIO DADO
SE COLOCAN PARA MANEJAR LA CONSISTENCIA ENTRE LAS VISTAS Y EL CÓDIGO
public void autoConfirmar(Estado autoconfirmado) { setEstado(autoconfirmado); }

public void autoDetectar(Estado autodetectado) { setEstado(autodetectado); }

public void marcarPendienteDeRevision(Estado pendienteDeRevision) { setEstado(pendienteDeRevision); }

public void derivarASupervisor(Estado aSupervisor) { setEstado(aSupervisor); }

public void confirmar(Estado confirmado) { setEstado(confirmado); }

public void marcarSinRevision(Estado sinRevision) { setEstado(sinRevision); }

public void marcarPendienteDeCierre(Estado pendienteDeCierre) { setEstado(pendienteDeCierre); }

public void marcarCierreDeEvento(Estado cierreDeEvento) { setEstado(cierreDeEvento); }
*/