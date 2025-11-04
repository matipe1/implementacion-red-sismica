package ar.edu.utn.dsi.ppai.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDTO;
import ar.edu.utn.dsi.ppai.entities.dtos.EventoSismicoDetalleDTO;
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

    @PostMapping("/eventos/{id}/seleccionar")
    public ResponseEntity<?> tomarSeleccionDeEvento(@PathVariable Long id) {
        try {
            EventoSismicoDetalleDTO eventoSeleccionado = servicioRegistroRevision.tomarSeleccionDeEvento(id);
            return ResponseEntity.ok(eventoSeleccionado);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/eventos/{id}/rechazar")
    public ResponseEntity<Void> tomarRechazoDeEvento(@PathVariable Long eventoId) {
        try {
            servicioRegistroRevision.tomarRechazoDeEvento(eventoId);
            return ResponseEntity.ok().build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
