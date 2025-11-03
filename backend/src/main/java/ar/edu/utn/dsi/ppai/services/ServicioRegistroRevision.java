package ar.edu.utn.dsi.ppai.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;
import ar.edu.utn.dsi.ppai.entities.estados.Estado;
import ar.edu.utn.dsi.ppai.repositories.EstadoRepository;
import ar.edu.utn.dsi.ppai.repositories.EventoSismicoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioRegistroRevision {
    private final EventoSismicoRepository eventoSismicoRepository;
    private final EstadoRepository estadoRepository;
    private final ServicioDeAutenticacion authService;

    public ServicioRegistroRevision(EventoSismicoRepository eventoSismicoRepository,
                                   EstadoRepository estadoRepository,
                                   ServicioDeAutenticacion authService) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.estadoRepository = estadoRepository;
        this.authService = authService;
    }

    public List<EventoSismicoDTO> opcionRegistrarRevisionManual() {
        Stream<EventoSismicoDTO> eventos = this.buscarEventosAutoDetectados();
        return this.ordenarEventosSismicos(eventos);
    }

    public Stream<EventoSismicoDTO> buscarEventosAutoDetectados() {
        Estado estadoAutodetectado = estadoRepository.findByNombre("Autodetectado")
                    .orElseThrow(() -> new EntityNotFoundException("El estado 'Autodetectado' no existe en la base de datos."));

        Stream<EventoSismico> eventos = eventoSismicoRepository.findByEstadoActual(estadoAutodetectado); // mas eficiente que hacer findAll y preguntarle a cada estado
        return eventos
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

    // EventoSismicoDetalleDTO tomarSeleccionDeEvento(Long eventoId) {

    // }

    // public void rechazarEventoSismico(Long eventoId) {
    // EventoSismico evento =
    // Estado estadoRechazado =

    // Empleado empleadoLogueado = authService.getEmpleadoLogueado();
    // evento.rechazar(estadoRechazado, LocalDateTime.now(), empleadoLogueado);

    // eventoSismicoReposotory.save(evento);
}
