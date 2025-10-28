package ar.edu.utn.dsi.ppai.services;

import org.springframework.stereotype.Service;
import java.util.List;
import ar.edu.utn.dsi.ppai.entities.AlcanceSismo;
import ar.edu.utn.dsi.ppai.repositories.AlcanceSismoRepository;

// Servicio de prueba
@Service
public class AlcanceSismoService {

    private final AlcanceSismoRepository alcanceSismoRepository;

    public AlcanceSismoService(AlcanceSismoRepository alcanceSismoRepository) {
        this.alcanceSismoRepository = alcanceSismoRepository;
    }

    public List<AlcanceSismo> findAll() {
        return alcanceSismoRepository.findAll(); // creo que deberia devolver un DTO acá (o nose si en el gestor convertirlo)
    }

    public AlcanceSismo save(AlcanceSismo alcance) {
        return alcanceSismoRepository.save(alcance);
    }
}