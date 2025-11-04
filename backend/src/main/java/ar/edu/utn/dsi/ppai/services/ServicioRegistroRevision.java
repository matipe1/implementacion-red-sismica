package ar.edu.utn.dsi.ppai.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ar.edu.utn.dsi.ppai.entities.DetalleMuestraSismica;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import ar.edu.utn.dsi.ppai.entities.SerieTemporal;
import ar.edu.utn.dsi.ppai.entities.Sismografo;
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

    public Stream<EventoSismicoDTO> buscarEventosAutoDetectados() {
        Estado estadoAutodetectado = estadoRepository.findByNombre("Autodetectado")
                    .orElseThrow(() -> new EntityNotFoundException("El estado 'Autodetectado' no existe en la base de datos."));

        List<EventoSismico> eventos = eventoSismicoRepository.findByEstadoActual(estadoAutodetectado); // mas eficiente que hacer findAll y preguntarle a cada estado
        return eventos.stream()
            .map(evento -> {
                    return EventoSismicoDTO.builder()
                            .id(evento.getId())
                            .fechaHoraOcurrencia(evento.getFechaHoraOcurrencia())
                            .ubicacion(evento.getUbicacion())
                            .valorMagnitud(evento.getValorMagnitud())
                            .build();
                });
    }
    
    public List<EventoSismicoDTO> ordenarEventosSismicos(Stream<EventoSismicoDTO> eventosDesordenados) { // ordenarPorFechaHora() <-- sería más legible
        return eventosDesordenados
            .sorted(Comparator.comparing(EventoSismicoDTO::getFechaHoraOcurrencia))
            .collect(Collectors.toList());
    }

    public EventoSismicoDetalleDTO tomarSeleccionDeEvento(Long eventoId) {
        EventoSismico eventoSeleccionado = eventoSismicoRepository.findById(eventoId)
            .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        this.bloquearEventoSismico(eventoSeleccionado);

        DatosSismicosDTO datosSismicos = this.buscarDatosSismicosDeEvento(eventoSeleccionado);
        List<SerieTemporalDTO> seriesTemporales = this.buscarSeriesTemporalesClasificadas(eventoSeleccionado);

        return new EventoSismicoDetalleDTO(
            eventoSeleccionado.getId(),
            eventoSeleccionado.getFechaHoraOcurrencia(),
            eventoSeleccionado.getUbicacion(),
            eventoSeleccionado.getValorMagnitud(),
            datosSismicos,
            seriesTemporales
        );
    }

    private void bloquearEventoSismico(EventoSismico evento) {
        LocalDateTime fechaHoraActual = this.obtenerFechaHoraActual();
        evento.bloquear(fechaHoraActual, null);
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
                                        detalle.getTipoDeDato().getNombreUnidadMedida(),
                                        detalle.getTipoDeDato().getValor()
                                    )
                                ))
                                .toList()
                        ))
                        .toList()
                ))
            )
            .toList();
    }

    private void rechazarEventoSismico(EventoSismico evento) {
        LocalDateTime fechaHoraActual = this.obtenerFechaHoraActual();
        Empleado empleadoLogueado = authService.getASLogueado();
        evento.rechazar(fechaHoraActual, empleadoLogueado);
    }
}
