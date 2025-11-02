package ar.edu.utn.dsi.ppai.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.edu.utn.dsi.ppai.entities.EventoSismico;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
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

    public List<EventoSismicoDTO> buscarEventosParaRevision() {
        Estado estadoAutodetectado = estadoRepository.findByNombre("Autodetectado")
            .orElseThrow(() -> new EntityNotFoundException("El estado 'Autodetectado' no existe en la base de datos."));

        List<EventoSismico> eventos = eventoSismicoRepository.findByEstadoActual(estadoAutodetectado);
        // Caso alternativo: No hay eventos
        if (eventos.isEmpty()) {
            return new ArrayList<>();
        }
        // El caso de uso pide ordenar? podriamos meter un .sorted si lo pide
        // creo que habia que implementar el sorted pero en un metodo...
        return eventos.stream()
                .map(evento -> {
                    return EventoSismicoDTO.builder()
                            .id(evento.getId())
                            .fechaHoraOcurrencia(evento.getFechaHoraOcurrencia())
                            .ubicacion(evento.getUbicacion())
                            .valorMagnitud(evento.getValorMagnitud())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // public void rechazarEventoSismico(Long eventoId) {
    // EventoSismico evento =
    // Estado estadoRechazado =

    // Empleado empleadoLogueado = authService.getEmpleadoLogueado();
    // evento.rechazar(estadoRechazado, LocalDateTime.now(), empleadoLogueado);

    // eventoSismicoReposotory.save(evento);
}
