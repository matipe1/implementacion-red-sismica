package ar.edu.utn.dsi.ppai.repositories;


import ar.edu.utn.dsi.ppai.entities.SerieTemporal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieTemporalRepository extends JpaRepository<SerieTemporal, Long> {

}

