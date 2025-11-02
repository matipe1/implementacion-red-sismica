package ar.edu.utn.dsi.ppai.repositories;


import ar.edu.utn.dsi.ppai.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
    
}
