package ar.edu.utn.dsi.ppai.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.dsi.ppai.entities.AlcanceSismo;
import ar.edu.utn.dsi.ppai.services.AlcanceSismoService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Controlador de prueba
@RestController
@RequestMapping("/api/alcances")
public class AlcanceSismoController {
    private final AlcanceSismoService alcanceSismoService;

    public AlcanceSismoController(AlcanceSismoService alcanceSismoService) {
        this.alcanceSismoService = alcanceSismoService;
    }

    @GetMapping
    public List<AlcanceSismo> getAll() {
        return alcanceSismoService.findAll(); // deberia devolver un DTO pero es para probar
    }

    @PostMapping
    public AlcanceSismo create(@RequestBody AlcanceSismo alcance) { // debería ser DTO pero es para probar
        return alcanceSismoService.save(alcance);
    }
}
