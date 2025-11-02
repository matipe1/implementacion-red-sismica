package ar.edu.utn.dsi.ppai.services;

import org.springframework.stereotype.Service;
import ar.edu.utn.dsi.ppai.entities.Empleado;
import ar.edu.utn.dsi.ppai.entities.Sesion;
import ar.edu.utn.dsi.ppai.repositories.SesionRepository;

@Service
public class ServicioDeAutenticacion {
    private final SesionRepository sesionRepository;
    private static final Long SESION_ACTIVA_ID = 1L;

    public ServicioDeAutenticacion(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    public Empleado getASLogueado() {
        Sesion sesionActiva = sesionRepository.findById(SESION_ACTIVA_ID)
            .orElseThrow(() -> new RuntimeException("Error de simulación: No se encontró la sesión con ID " + SESION_ACTIVA_ID));
    
        Empleado empleado = sesionActiva.buscarASLogueado();
        if (empleado == null) throw new RuntimeException("El usuario de la sesión no tiene un empleado asociado");
        return empleado;
    }

    // public Empleado getEmpleadoLogueado() {
    //     return Empleado.builder()
    //             .nombre("Maravilla")
    //             .apellido("Martinez")
    //             .mail("maravilla@gmail.com")
    //             .telefono("+543511234567")
    //             .build();
    // }
}
