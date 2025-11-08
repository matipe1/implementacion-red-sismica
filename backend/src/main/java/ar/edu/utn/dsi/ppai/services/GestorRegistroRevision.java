package ar.edu.utn.dsi.ppai.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import ar.edu.utn.dsi.ppai.entities.SerieTemporal;
import ar.edu.utn.dsi.ppai.entities.Sesion;
import ar.edu.utn.dsi.ppai.entities.Sismografo;
import ar.edu.utn.dsi.ppai.entities.dtos.CambioEstadoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.DatosSismicosDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.DetalleMuestraSismicaDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.MuestraSismicaDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.SerieTemporalDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.TipoDeDatoDTO;
import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import ar.edu.utn.dsi.ppai.repositories.EstadoRepository;
import ar.edu.utn.dsi.ppai.repositories.EventoSismicoRepository;
import ar.edu.utn.dsi.ppai.repositories.SesionRepository;
import ar.edu.utn.dsi.ppai.repositories.SismografoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class GestorRegistroRevision {
    private final EventoSismicoRepository eventoSismicoRepository;
    private final EstadoRepository estadoRepository;
    private final SismografoRepository sismografoRepository;
    private final SesionRepository sesionRepository;
    private static final Long SESION_ACTIVA_ID = 1L;

    public GestorRegistroRevision(EventoSismicoRepository eventoSismicoRepository,
                                   EstadoRepository estadoRepository,
                                   SismografoRepository sismografoRepository,
                                   SesionRepository sesionRepository) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.estadoRepository = estadoRepository;
        this.sismografoRepository = sismografoRepository;
        this.sesionRepository = sesionRepository;
    }

    public List<EventoSismicoDTO> opcionRegistrarRevisionManual() {
        Stream<EventoSismicoDTO> StreamEventosAutodetectadosDTO = this.buscarEventosAutoDetectados();
        return this.ordenarEventosSismicos(StreamEventosAutodetectadosDTO);
    }
    
    @Transactional
    public EventoSismicoDetalleDTO tomarSeleccionDeEvento(
            LocalDateTime fechaHoraOcurrencia,
            BigDecimal latitudEpicentro,
            BigDecimal longitudEpicentro,
            BigDecimal latitudHipocentro,
            BigDecimal longitudHipocentro,
            BigDecimal valorMagnitud) {

        EventoSismico eventoSeleccionado = eventoSismicoRepository.findByDatosBase(
            fechaHoraOcurrencia, latitudEpicentro, longitudEpicentro, latitudHipocentro, longitudHipocentro, valorMagnitud
        ).orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        try {
            this.bloquearEventoSismico(eventoSeleccionado);
            eventoSismicoRepository.save(eventoSeleccionado);
            
        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException(e);
        }
        DatosSismicosDTO datosExtra = this.buscarDatosSismicosDeEvento(eventoSeleccionado);
        // Este debería implementarse como dice en el diagrama de secuencia:
        List<SerieTemporalDTO> seriesTemporales = this.buscarSeriesTemporalesClasificadas(eventoSeleccionado);
        this.llamarCUGenerarSismograma(seriesTemporales);

        // Si el frontend puede guardar el estado NO LE PASAMOS DE VUELTA LOS DATOS BASE (PREGUNTAR AL FRONTEND)
        EventoSismicoDTO datosBase = this.construirEventoSismicoDTO(eventoSeleccionado);
        // Método de prueba para verificar el cambio de estado (ELIMINAR UNA VEZ CORROBORADO PATRON STATE)
        List<CambioEstadoDTO> cambiosEstadoPrueba = this.cambiosEstadoPrueba(eventoSeleccionado);

        return new EventoSismicoDetalleDTO(datosBase, datosExtra, seriesTemporales, cambiosEstadoPrueba);
    }

    // Fijarse si debería retornar algo luego de rechazar (SI LO NECESITA EL FRONTEND)
    @Transactional
    public void tomarRechazoDeEvento(
            LocalDateTime fechaHoraOcurrencia,
            BigDecimal latitudEpicentro,
            BigDecimal longitudEpicentro,
            BigDecimal latitudHipocentro,
            BigDecimal longitudHipocentro,
            BigDecimal valorMagnitud) {
                
        EventoSismico eventoSeleccionado = eventoSismicoRepository.findByDatosBase(
            fechaHoraOcurrencia, latitudEpicentro, longitudEpicentro, latitudHipocentro, longitudHipocentro, valorMagnitud)
            .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        try {
            this.rechazarEventoSismico(eventoSeleccionado);
            eventoSismicoRepository.save(eventoSeleccionado);

        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Stream<EventoSismicoDTO> buscarEventosAutoDetectados() {
        Estado estadoAutodetectado = estadoRepository.findByNombre("Autodetectado")
                    .orElseThrow(() -> new EntityNotFoundException("El estado 'Autodetectado' no existe en la base de datos."));

        List<EventoSismico> eventosAutodetectados = eventoSismicoRepository.findByEstadoActual(estadoAutodetectado);
        return eventosAutodetectados.stream()
            .map(evento -> this.construirEventoSismicoDTO(evento));
    }

    private List<EventoSismicoDTO> ordenarEventosSismicos(Stream<EventoSismicoDTO> eventosDesordenados) {
        return eventosDesordenados
            .sorted(Comparator.comparing(EventoSismicoDTO::getFechaHoraOcurrencia))
            .collect(Collectors.toList());
    }

    private void bloquearEventoSismico(EventoSismico evento) {

        LocalDateTime fechaHoraActual = this.obtenerFechaHoraActual();
        evento.bloquear(fechaHoraActual);
    }

    private void rechazarEventoSismico(EventoSismico evento) {

        LocalDateTime fechaHoraActual = this.obtenerFechaHoraActual();
        Empleado empleadoLogueado = this.buscarASLogueado();

        evento.rechazar(fechaHoraActual, empleadoLogueado);
    }

    private void llamarCUGenerarSismograma(List<SerieTemporalDTO> seriesTemporalesClasificadas) {
        System.out.println("Se llamó al CU 'Generar Sismograma' pasandole la lista de series clasificadas por estacion.");
    }

    private LocalDateTime obtenerFechaHoraActual() {
        return LocalDateTime.now();
    }

    private Empleado buscarASLogueado() {
        Sesion sesionActiva = sesionRepository.findById(SESION_ACTIVA_ID)
            .orElseThrow(() -> new RuntimeException("Error de simulación: No se encontró la sesión con ID " + SESION_ACTIVA_ID));
    
        Empleado empleado = sesionActiva.buscarASLogueado();
        if (empleado == null) throw new RuntimeException("El usuario de la sesión no tiene un empleado asociado");
        return empleado;
    }

    // Metodos para obtener datos necesarios, construir y devolver DTOs
    private EventoSismicoDTO construirEventoSismicoDTO(EventoSismico evento) {
        return EventoSismicoDTO.builder()
                            .fechaHoraOcurrencia(evento.getFechaHoraOcurrencia())
                            .latitudEpicentro(evento.getLatitudEpicentro())
                            .longitudEpicentro(evento.getLongitudEpicentro())
                            .latitudHipocentro(evento.getLatitudHipocentro())
                            .longitudHipocentro(evento.getLongitudHipocentro())
                            .valorMagnitud(evento.getValorMagnitud())
                            .build();
    }

    private DatosSismicosDTO buscarDatosSismicosDeEvento(EventoSismico evento) {
        return new DatosSismicosDTO(
            evento.getAlcanceSismo().getNombre(),
            evento.getClasificacionSismo().getNombre(),
            evento.getOrigenDeGeneracion().getNombre()
        );
    }

    // DEBEMOS IMPLEMENTARLA COMO DICE EN EL DIAGRAMA DE SECUENCIA (o corroborar si está implementada así)
    private List<SerieTemporalDTO> buscarSeriesTemporalesClasificadas(EventoSismico evento) {
        List<Sismografo> sismografos = sismografoRepository.findAll();
        Map<Integer, List<SerieTemporal>> seriesClasificadas = evento.obtenerSeriesTemporalesClasificadas(sismografos);

        return seriesClasificadas.entrySet().stream()
            .flatMap(entry -> entry.getValue().stream()
                .map(serie -> new SerieTemporalDTO(
                    entry.getKey(), // código de estación
                    serie.getMuestrasSismicas().stream()
                        .map(muestra -> new MuestraSismicaDTO(
                            muestra.getFechaHoraMuestra(),
                            muestra.getDetallesMuestrasSismicas().stream()
                                .map(detalle -> new DetalleMuestraSismicaDTO(
                                    detalle.getValor(),
                                    new TipoDeDatoDTO(
                                        detalle.getTipoDeDato().getDenominacion(),
                                        detalle.getTipoDeDato().getNombreUnidadMedida()
                                    )
                                ))
                                .toList()
                        ))
                        .toList()
                ))
            )
            .toList();
    }



    // Se deberian borrar cuando arreglemos el tema del patron state
    private Estado buscarEstadoBloqueadoEnRevision(EventoSismico evento) {
        return estadoRepository.findByNombre("Bloqueado en revisión")
                    .orElseThrow(() -> new EntityNotFoundException("El estado 'Bloqueado en revisión' no existe en la base de datos."));
    }

    private Estado buscarEstadoRechazado(EventoSismico evento) {
        return estadoRepository.findByNombre("Rechazado")
            .orElseThrow(() -> new EntityNotFoundException("El estado 'Rechazado' no existe en la base de datos."));
    }

    // Prueba (tambien se deberia borrar)
    private List<CambioEstadoDTO> cambiosEstadoPrueba(EventoSismico eventoSeleccionado) {
        return eventoSeleccionado.getCambiosDeEstado().stream()
            .map(cambio -> new CambioEstadoDTO(
                cambio.getFechaHoraDesde(),
                cambio.getFechaHoraHasta(),
                cambio.getResponsableInspeccion() != null
                    ? cambio.getResponsableInspeccion().getNombre() + " " + cambio.getResponsableInspeccion().getApellido()
                    : null,
                cambio.getEstado().getNombre()
            ))
            .toList();
    }
}
