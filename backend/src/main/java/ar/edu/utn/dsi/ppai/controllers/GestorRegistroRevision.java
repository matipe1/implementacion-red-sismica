package ar.edu.utn.dsi.ppai.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;
import ar.edu.utn.dsi.ppai.services.ServicioRegistroRevision;

// import application.InterfazRegistroRevision;

// import javafx.collections.ObservableList;
// import models.Empleado;
// import models.Estado;
// import models.EventoSismico;
// import models.Sesion;
// import models.SerieTemporal;
// import models.Sismografo;
// import utils.DatosSimulados;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import java.util.Map;

// GENERO NUEVAMENTE PARA NO BORRAR LO ANTERIOR Y TENERLO DE REFERENCIA
@RestController
@RequestMapping("/api/revisiones")
public class GestorRegistroRevision {
    private final ServicioRegistroRevision servicioRegistroRevision;

    public GestorRegistroRevision(ServicioRegistroRevision servicioRegistroRevision) {
        this.servicioRegistroRevision = servicioRegistroRevision;
    }

    @GetMapping("/eventos-autodetectados")
    public ResponseEntity<List<EventoSismicoDTO>> opcionRegistrarRevisionManual() {
        List<EventoSismicoDTO> eventos = servicioRegistroRevision.opcionRegistrarRevisionManual();
        return eventos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(eventos);
    }

    @PostMapping("/eventos/{id}/seleccionar")
    public ResponseEntity<EventoSismicoDetalleDTO> tomarSeleccionDeEvento(@PathVariable Long id) {
        EventoSismicoDetalleDTO eventoSeleccionado = servicioRegistroRevision.tomarSeleccionDeEvento(id);
        if (eventoSeleccionado == null) {
        return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventoSeleccionado);
    }
}

// public class GestorRegistroRevision {
// private static final ObservableList<EventoSismico> ALL_EVENTOS =
// DatosSimulados.obtenerEventosSismicos();
// private static final List<Estado> ALL_ESTADOS =
// DatosSimulados.obtenerEstados();
// private static EventoSismico eventoSeleccionadoActual;
// private static Sesion SESION_ACTUAL = DatosSimulados.obtenerSesionActual();
// private static final List<Sismografo> ALL_SISMOGRAFOS =
// DatosSimulados.obtenerSismografos();
// public static void opcionRegistrarRevisionManual() {
// List<EventoSismico> eventosSismicosAutoDetectados =
// buscarEventosAutoDetectados(ALL_EVENTOS);
// List<EventoSismico> eventosOrdenados =
// ordenarEventosSismicos(eventosSismicosAutoDetectados);
// InterfazRegistroRevision.mostrarEventosEncontrados(eventosOrdenados);
// }
// private static List<EventoSismico>
// buscarEventosAutoDetectados(List<EventoSismico> eventosSismicos) {
// List<EventoSismico> eventosSismicosAutoDetectados = new ArrayList<>();
// for (EventoSismico e : eventosSismicos)
// if (e.estaAutoDetectado()) { eventosSismicosAutoDetectados.add(e); }
// return eventosSismicosAutoDetectados;
// }
// private static List<EventoSismico> ordenarEventosSismicos(List<EventoSismico>
// eventosAutodetectados) {
// eventosAutodetectados.sort(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia));
// return eventosAutodetectados;
// }

// public static void tomarSeleccionDeEvento(EventoSismico eventoSeleccionado) {
// eventoSeleccionadoActual = eventoSeleccionado;

// bloquearEventoSismico(eventoSeleccionado);
// String[] datosSismicosDeEventoSeleccionado =
// buscarDatosSismicosDeEvento(eventoSeleccionado);

// InterfazRegistroRevision.mostrarEventoSeleccionado(eventoSeleccionado);
// InterfazRegistroRevision.mostrarDatosSismicos(datosSismicosDeEventoSeleccionado);

// Map<Integer, List<SerieTemporal>> seriesTemporalesClasificadas =
// buscarSeriesTemporalesClasificadas(eventoSeleccionado);

// if (llamarCUGenerarSismograma(seriesTemporalesClasificadas)){
// System.out.println("Se llamó al CU Generar Sismograma pasándole la lista de
// muestras.");
// }
// }

// // ESTE METODO SERIA EL DISPARADOR PARA OBTENER UN MAP CON LAS SERIES
// TEMPORALES CLASIFICADAS
// public static Map<Integer, List<SerieTemporal>>
// buscarSeriesTemporalesClasificadas(EventoSismico eventoSeleccionado) {
// return
// eventoSeleccionado.obtenerSeriesTemporalesClasificadas(ALL_SISMOGRAFOS);
// }

// private static Estado buscarEstadoBloqueadoEnRevision() {
// for (Estado e : ALL_ESTADOS) {
// if (e.esAmbitoEventoSismico() && e.esBloqueadoEnRevision()){ return e; }
// }
// return null;
// }

// private static void bloquearEventoSismico(EventoSismico eventoSeleccionado) {
// Estado estadoBloqueadoEnRevision = buscarEstadoBloqueadoEnRevision();
// LocalDateTime fechaHoraActual = obtenerFechaHoraActual();
// eventoSeleccionado.bloquear(estadoBloqueadoEnRevision, fechaHoraActual,
// null);
// }

// private static LocalDateTime obtenerFechaHoraActual() {
// return LocalDateTime.now();
// }

// private static String[] buscarDatosSismicosDeEvento(EventoSismico
// eventoSeleccionado) {
// return new String[] {
// eventoSeleccionado.getAlcanceSismo().getNombre(),
// eventoSeleccionado.getClasificacionSismo().getNombre(),
// eventoSeleccionado.getOrigenDeGeneracion().getNombre()
// }; }

// /* Este metodo le envía al caso de uso generar sismograma una lista
// con las series temporales clasificadas por codigo de estacion para que pueda
// operar*/
// private static Boolean llamarCUGenerarSismograma(Map<Integer,
// List<SerieTemporal>> seriesTemporalesClasificadas){
// return (seriesTemporalesClasificadas != null);
// }

// public static void tomarRechazoDeEvento() {
// if (validarDatosSismicos(eventoSeleccionadoActual) && validarSeleccion()) {
// rechazarEventoSismico(eventoSeleccionadoActual);

// finCU();
// }
// }

// private static Boolean validarDatosSismicos(EventoSismico eventoSeleccionado)
// {
// return (eventoSeleccionado.getValorMagnitud() != null &&
// eventoSeleccionado.getAlcanceSismo() != null &&
// eventoSeleccionado.getOrigenDeGeneracion() != null);
// }

// private static Boolean validarSeleccion() {
// return true;
// }

// private static void rechazarEventoSismico(EventoSismico eventoSeleccionado){
// Estado estadoRechazado = buscarEstadoRechazado();
// LocalDateTime fechaHoraActual = obtenerFechaHoraActual();
// Empleado empleadoLogueado = buscarASLogueado();
// eventoSeleccionado.rechazar(estadoRechazado, fechaHoraActual,
// empleadoLogueado);
// }

// private static Estado buscarEstadoRechazado() {
// for (Estado e : ALL_ESTADOS) {
// if (e.esAmbitoEventoSismico() && e.esRechazado()){ return e; }
// }
// return null;
// }

// private static Empleado buscarASLogueado() {
// return SESION_ACTUAL.buscarASLogueado();
// }

// public static void finCU() {
// System.out.println("Se ha ejecutado el CU Registrar resultado de revisión
// manual con éxito.");
// }
// }