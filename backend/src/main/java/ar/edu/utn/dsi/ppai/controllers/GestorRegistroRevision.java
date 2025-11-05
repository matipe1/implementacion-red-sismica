package ar.edu.utn.dsi.ppai.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.CoordenadasAproximadasDTO;
import ar.edu.utn.dsi.ppai.services.ServicioRegistroRevision;
import jakarta.persistence.EntityNotFoundException;

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

    @PostMapping("/eventos/seleccionar")
    public ResponseEntity<?> tomarSeleccionDeEvento(@RequestBody CoordenadasAproximadasDTO request) {
        try {
            EventoSismicoDetalleDTO eventoSeleccionado =
                servicioRegistroRevision.tomarSeleccionDeEvento(
                    request.getLatitudEpicentro(),
                    request.getLongitudEpicentro(),
                    request.getLatitudHipocentro(),
                    request.getLongitudHipocentro()
                );
            return ResponseEntity.ok(eventoSeleccionado);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/eventos/rechazar")
    public ResponseEntity<Void> tomarRechazoDeEvento(@RequestBody CoordenadasAproximadasDTO request) {
        try {
            servicioRegistroRevision.tomarRechazoDeEvento(
                    request.getLatitudEpicentro(),
                    request.getLongitudEpicentro(),
                    request.getLatitudHipocentro(),
                    request.getLongitudHipocentro()
                );
            return ResponseEntity.ok().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
