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
import ar.edu.utn.dsi.ppai.services.GestorRegistroRevision;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/revisiones")
public class InterfazRegistroRevision {
    private final GestorRegistroRevision gestorRegistroRevision;

    public InterfazRegistroRevision(GestorRegistroRevision gestorRegistroRevision) {
        this.gestorRegistroRevision = gestorRegistroRevision;
    }

    @GetMapping("/eventos-autodetectados")
    public ResponseEntity<List<EventoSismicoDTO>> opcionRegistrarRevisionManual() {
        // habilitarVentana();
        List<EventoSismicoDTO> eventos = gestorRegistroRevision.opcionRegistrarRevisionManual();
        return eventos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(eventos);
    }

    @PostMapping("/eventos/seleccionar")
    public ResponseEntity<?> tomarSeleccionDeEvento(@RequestBody EventoSismicoDTO eventoSeleccionado) {
        try {
            EventoSismicoDetalleDTO eventoSeleccionadoDetallado = gestorRegistroRevision.tomarSeleccionDeEvento(
                    eventoSeleccionado.getFechaHoraOcurrencia(),
                    eventoSeleccionado.getLatitudEpicentro(),
                    eventoSeleccionado.getLongitudEpicentro(),
                    eventoSeleccionado.getLatitudHipocentro(),
                    eventoSeleccionado.getLongitudHipocentro(),
                    eventoSeleccionado.getValorMagnitud()
                );
            return ResponseEntity.ok(eventoSeleccionadoDetallado);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/eventos/rechazar")
    public ResponseEntity<Void> tomarRechazoDeEvento(@RequestBody EventoSismicoDTO eventoSeleccionado) {
        try {
            gestorRegistroRevision.tomarRechazoDeEvento(
                    eventoSeleccionado.getFechaHoraOcurrencia(),
                    eventoSeleccionado.getLatitudEpicentro(),
                    eventoSeleccionado.getLongitudEpicentro(),
                    eventoSeleccionado.getLatitudHipocentro(),
                    eventoSeleccionado.getLongitudHipocentro(),
                    eventoSeleccionado.getValorMagnitud()
                );
            return ResponseEntity.ok().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
