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
import ar.edu.utn.dsi.ppai.entities.estados.AutoDetectado;
import ar.edu.utn.dsi.ppai.repositories.EventoSismicoRepository;
import ar.edu.utn.dsi.ppai.repositories.SesionRepository;
import ar.edu.utn.dsi.ppai.repositories.SismografoRepository;
import ar.edu.utn.dsi.ppai.services.interfaces.IGestorRegistroRevision;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class GestorRegistroRevision implements IGestorRegistroRevision {
    private final EventoSismicoRepository eventoSismicoRepository;
    private final SismografoRepository sismografoRepository;
    private final SesionRepository sesionRepository;
    private static final Long SESION_ACTIVA_ID = 1L;

    public GestorRegistroRevision(EventoSismicoRepository eventoSismicoRepository,
                                   SismografoRepository sismografoRepository,
                                   SesionRepository sesionRepository) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.sismografoRepository = sismografoRepository;
        this.sesionRepository = sesionRepository;
    }

    public List<EventoSismicoDTO> opcionRegistrarRevisionManual() {
        Stream<EventoSismicoDTO> StreamEventosAutodetectadosDTO = this.buscarEventosAutoDetectados();
        return this.ordenarEventosSismicos(StreamEventosAutodetectadosDTO);
    }
    
    @Transactional
    public EventoSismicoDetalleDTO tomarSeleccionDeEvento(EventoSismicoDTO eventoDTO) {
        try {
            EventoSismico eventoSeleccionado = eventoSismicoRepository.findByDatosBase(
                eventoDTO.getFechaHoraOcurrencia(),
                eventoDTO.getLatitudEpicentro(), eventoDTO.getLongitudEpicentro(),
                eventoDTO.getLatitudHipocentro(), eventoDTO.getLongitudHipocentro(),
                eventoDTO.getValorMagnitud())
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
            
            this.bloquearEventoSismico(eventoSeleccionado);
            eventoSismicoRepository.save(eventoSeleccionado);

            DatosSismicosDTO datosSismicosDeEvento = this.buscarDatosSismicosDeEvento(eventoSeleccionado);
            List<SerieTemporalDTO> seriesTemporales = this.buscarSeriesTemporalesClasificadas(eventoSeleccionado);
            this.llamarCUGenerarSismograma(seriesTemporales);

            // Método de prueba para verificar el cambio de estado (ELIMINAR UNA VEZ CORROBORADO PATRON STATE)
            List<CambioEstadoDTO> cambiosEstadoPrueba = this.cambiosEstadoPrueba(eventoSeleccionado);

            return new EventoSismicoDetalleDTO(datosSismicosDeEvento, seriesTemporales, cambiosEstadoPrueba);
            
        } catch (EntityNotFoundException e) {
            throw e;
            
        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Transactional
    public void tomarRechazoDeEvento(EventoSismicoDTO eventoDTO) {
        try {
            EventoSismico eventoSeleccionado = eventoSismicoRepository.findByDatosBase(
                eventoDTO.getFechaHoraOcurrencia(),
                eventoDTO.getLatitudEpicentro(),
                eventoDTO.getLongitudEpicentro(),
                eventoDTO.getLatitudHipocentro(),
                eventoDTO.getLongitudHipocentro(),
                eventoDTO.getValorMagnitud())
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
            
            this.rechazarEventoSismico(eventoSeleccionado);
            eventoSismicoRepository.save(eventoSeleccionado);

        } catch (EntityNotFoundException e) {
            throw e;
            
        } catch (UnsupportedOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private Stream<EventoSismicoDTO> buscarEventosAutoDetectados() {
        return eventoSismicoRepository.findByEstadoActualInstanceOf(AutoDetectado.class).stream()
            .map(evento -> EventoSismicoDTO.builder()
                            .fechaHoraOcurrencia(evento.getFechaHoraOcurrencia())
                            .latitudEpicentro(evento.getLatitudEpicentro())
                            .longitudEpicentro(evento.getLongitudEpicentro())
                            .latitudHipocentro(evento.getLatitudHipocentro())
                            .longitudHipocentro(evento.getLongitudHipocentro())
                            .valorMagnitud(evento.getValorMagnitud())
                            .build());
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
