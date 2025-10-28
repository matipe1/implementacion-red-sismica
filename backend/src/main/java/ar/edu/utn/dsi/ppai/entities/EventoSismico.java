package ar.edu.utn.dsi.ppai.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EventoSismico {
    private LocalDateTime fechaHoraOcurrencia;
    private LocalDateTime fechaHoraFin;
    private Double latitudEpicentro;
    private Double longitudEpicentro;
    private Double latitudHipocentro;
    private Double longitudHipocentro;
    private Double valorMagnitud;
    private List<SerieTemporal> seriesTemporales;
    private Empleado analistaSupervisor;
    private List<CambioEstado> cambiosDeEstado;
    private Estado estadoActual;
    private ClasificacionSismo clasificacionSismo;
    private OrigenDeGeneracion origenDeGeneracion;
    private AlcanceSismo alcanceSismo;

    public Boolean estaAutoDetectado() { 
        return (estadoActual.esAutoDetectado()); 
    }

    public String getFechaHoraOcurrencia() { DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHoraOcurrencia.format(formatter);
    }

    public String getUbicacion() {
        return "Epicentro: (" + latitudEpicentro + "," + longitudEpicentro + ") - "
                + "Hipocentro: (" + latitudHipocentro + "," + longitudHipocentro + ")";
    }

    public Double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setEstado(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    public CambioEstado buscarCEActual() {
        for (CambioEstado ce : this.cambiosDeEstado) {
            if (ce.sosCEActual()) {
                return ce;
            }
        }
        return null;
    }

    public void crearCEBloqueadoEnRevision(LocalDateTime fechaHoraActual, Estado estado, Empleado responsableInspeccion) {
        CambioEstado bloqEnRev = new CambioEstado(fechaHoraActual, null, null, estado);
        System.out.println("Tamaño antes de agregar el ce bloqueado en revision: " + cambiosDeEstado.size());
        cambiosDeEstado.add(bloqEnRev);
        System.out.println("Tamaño despues de agregar el ce bloqueado en revision: " + cambiosDeEstado.size());
    }

    public AlcanceSismo getAlcanceSismo() {
        return alcanceSismo;
    }

    public ClasificacionSismo getClasificacionSismo() {
        return clasificacionSismo;
    }

    public OrigenDeGeneracion getOrigenDeGeneracion() {
        return origenDeGeneracion;
    }

    public void rechazar(Estado rechazado, LocalDateTime fechaHoraActual, Empleado responsableInspeccion) {
        setEstado(rechazado);
        System.out.println("Estado seteado a rechazado");
        CambioEstado bloqueado = buscarCEActual();
        bloqueado.setFechaHoraHasta(fechaHoraActual);
        crearCERechazado(fechaHoraActual, rechazado, responsableInspeccion);
    }

    public void crearCERechazado(LocalDateTime fechaHoraActual, Estado estado, Empleado responsableInspeccion) {
        CambioEstado bloqEnRev = new CambioEstado(fechaHoraActual, null, responsableInspeccion, estado);
        System.out.println("Tamaño antes de agregar el ce rechazado: " + cambiosDeEstado.size());
        cambiosDeEstado.add(bloqEnRev);
        System.out.println("Tamaño despues de agregar el ce rechazado: " + cambiosDeEstado.size());
    }

    public void bloquear(Estado bloqueado, LocalDateTime fechaHoraActual, Empleado responsableInspeccion) {
        setEstado(bloqueado);
        System.out.println("Estado seteado a bloqueado");
        CambioEstado autoDetectado = buscarCEActual();
        autoDetectado.setFechaHoraHasta(fechaHoraActual);
        crearCEBloqueadoEnRevision(fechaHoraActual, bloqueado, null);
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