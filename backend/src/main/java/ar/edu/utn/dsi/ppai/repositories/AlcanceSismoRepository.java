package ar.edu.utn.dsi.ppai.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.edu.utn.dsi.ppai.entities.AlcanceSismo;

// Repositorio de prueba
@Repository
public interface AlcanceSismoRepository extends JpaRepository<AlcanceSismo, Integer> {
    // Sin metodos, solo hereda los de spring boot
}