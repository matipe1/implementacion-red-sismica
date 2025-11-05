package ar.edu.utn.dsi.ppai.services;

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
import ar.edu.utn.dsi.ppai.repositories.SismografoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ServicioRegistroRevision {
    private final EventoSismicoRepository eventoSismicoRepository;
    private final EstadoRepository estadoRepository;
    private final SismografoRepository sismografoRepository;
    private final ServicioDeAutenticacion authService;

    public ServicioRegistroRevision(EventoSismicoRepository eventoSismicoRepository,
                                   EstadoRepository estadoRepository,
                                   SismografoRepository sismografoRepository,
                                   ServicioDeAutenticacion authService) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.estadoRepository = estadoRepository;
        this.authService = authService;
        this.sismografoRepository = sismografoRepository;
    }

    public List<EventoSismicoDTO> opcionRegistrarRevisionManual() {
        Stream<EventoSismicoDTO> eventos = this.buscarEventosAutoDetectados();
        return this.ordenarEventosSismicos(eventos);
    }
    
    @Transactional
    public EventoSismicoDetalleDTO tomarSeleccionDeEvento(
            Double latitudEpicentro,
            Double longitudEpicentro,
            Double latitudHipocentro,
            Double longitudHipocentro) {
        
        EventoSismico eventoSeleccionado = eventoSismicoRepository.findByCoordenadasAproximadas(
                latitudEpicentro, longitudEpicentro, latitudHipocentro, longitudHipocentro)
            .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        try {
            this.bloquearEventoSismico(eventoSeleccionado);
            eventoSismicoRepository.save(eventoSeleccionado);
            
        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException(e);
        }

        DatosSismicosDTO datosSismicos = this.buscarDatosSismicosDeEvento(eventoSeleccionado);
        List<SerieTemporalDTO> seriesTemporales = this.buscarSeriesTemporalesClasificadas(eventoSeleccionado);
        this.llamarCUGenerarSismograma(seriesTemporales);

        // Prueba 
        List<CambioEstadoDTO> cambiosEstadoPrueba = this.cambiosEstadoPrueba(eventoSeleccionado);

        return new EventoSismicoDetalleDTO(
            eventoSeleccionado.getFechaHoraOcurrencia(),
            eventoSeleccionado.getUbicacion(),
            eventoSeleccionado.getValorMagnitud(),
            datosSismicos,
            seriesTemporales,
            cambiosEstadoPrueba // Prueba (sirven para validar como cambiaron los estados)
        );
    }

    @Transactional
    public void tomarRechazoDeEvento(
            Double latitudEpicentro,
            Double longitudEpicentro,
            Double latitudHipocentro,
            Double longitudHipocentro) {
                
        EventoSismico eventoSeleccionado = eventoSismicoRepository.findByCoordenadasAproximadas(
            latitudEpicentro, longitudEpicentro, latitudHipocentro, longitudHipocentro)
            .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        try {
            this.rechazarEventoSismico(eventoSeleccionado);
            eventoSismicoRepository.save(eventoSeleccionado);

        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Stream<EventoSismicoDTO> buscarEventosAutoDetectados() {
        // this.buscarEstadoAutodetectado();
        Estado estadoAutodetectado = estadoRepository.findByNombre("Autodetectado")
                    .orElseThrow(() -> new EntityNotFoundException("El estado 'Autodetectado' no existe en la base de datos."));

        List<EventoSismico> eventos = eventoSismicoRepository.findByEstadoActual(estadoAutodetectado);
        return eventos.stream()
            .map(evento -> {
                    return EventoSismicoDTO.builder()
                            .fechaHoraOcurrencia(evento.getFechaHoraOcurrencia())
                            .ubicacion(evento.getUbicacion())
                            .valorMagnitud(evento.getValorMagnitud())
                            .build();
                });
    }

    private List<EventoSismicoDTO> ordenarEventosSismicos(Stream<EventoSismicoDTO> eventosDesordenados) {
        return eventosDesordenados
            .sorted(Comparator.comparing(EventoSismicoDTO::getFechaHoraOcurrencia))
            .collect(Collectors.toList());
    }

    private void bloquearEventoSismico(EventoSismico evento) {
        Estado estadoBloqueadoEnRevision = this.buscarEstadoBloqueadoEnRevision(evento);

        LocalDateTime fechaHoraActual = this.obtenerFechaHoraActual();
        evento.bloquear(fechaHoraActual, estadoBloqueadoEnRevision);
    }

    private Estado buscarEstadoBloqueadoEnRevision(EventoSismico evento) {
        return estadoRepository.findByNombre("Bloqueado en revisión")
                    .orElseThrow(() -> new EntityNotFoundException("El estado 'Bloqueado en revisión' no existe en la base de datos."));
    }

    private LocalDateTime obtenerFechaHoraActual() {
        return LocalDateTime.now();
    }

    private DatosSismicosDTO buscarDatosSismicosDeEvento(EventoSismico evento) {
        return new DatosSismicosDTO(
            evento.getAlcanceSismo().getNombre(),
            evento.getClasificacionSismo().getNombre(),
            evento.getOrigenDeGeneracion().getNombre()
        );
    }

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

    // Prueba
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

    private void llamarCUGenerarSismograma(List<SerieTemporalDTO> seriesTemporalesClasificadas) {
        System.out.println("Se llamó al CU 'Generar Sismograma' pasandole la lista de series clasificadas por estacion.");
    }

    private void rechazarEventoSismico(EventoSismico evento) {

        Estado estadoRechazado = this.buscarEstadoRechazado(evento);

        LocalDateTime fechaHoraActual = this.obtenerFechaHoraActual();
        Empleado empleadoLogueado = authService.getASLogueado();

        evento.rechazar(fechaHoraActual, empleadoLogueado, estadoRechazado);
    }

    private Estado buscarEstadoRechazado(EventoSismico evento) {
        return estadoRepository.findByNombre("Rechazado")
            .orElseThrow(() -> new EntityNotFoundException("El estado 'Rechazado' no existe en la base de datos."));
    }
}
