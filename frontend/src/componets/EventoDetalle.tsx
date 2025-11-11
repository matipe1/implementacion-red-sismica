import type { Evento, DetalleEvento } from "../types/types";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

interface Props {
  evento: Evento | null;
  detalleEvento: DetalleEvento | null;
  isRejecting: boolean;
  onRechazar: () => void;
}

export default function EventoDetalle({ evento, detalleEvento, onRechazar, isRejecting }: Props) {
  const navigate = useNavigate();

  // Principio IHM: "Proteger al usuario de errores" (Si refresca la página)
  useEffect(() => {
    if (!evento || !detalleEvento) {
      navigate("/eventos-autodetectados");
    }
  }, [evento, detalleEvento, navigate]);

  if (!evento || !detalleEvento) {
    return null;
  }

  return (
    <div className="detalle-container">
      <h2>Evento Seleccionado</h2>
      
      <div className="bloque-info">
        <p>
          <strong>🕒 Fecha y hora:</strong> {evento.fechaHoraOcurrencia}
        </p>
        <p>
          <strong>🌋 Magnitud:</strong> {evento.valorMagnitud}
        </p>
        <div className="bloque-ubicacion">
          <strong>📍 Ubicación</strong>
          <ul className="list-unstyled mt-2 mb-0">
            <li><strong>- Epicentro:</strong> Latitud {evento.latitudEpicentro} - Longitud {evento.longitudEpicentro}</li>
            <li><strong>- Hipocentro:</strong> Latitud {evento.latitudHipocentro} - Longitud {evento.longitudHipocentro}</li>
          </ul>
        </div>
      </div>
        
      <h3 className="titulo-datos-sismicos">Datos Sísmicos del Evento</h3>
      <div className="bloque-info">
        <p>
          <strong>Origen:</strong> {detalleEvento.datosSismicos.origenDeGeneracion}
        </p>
        <p>
          <strong>Clasificación:</strong> {detalleEvento.datosSismicos.clasificacionSismo}
        </p>
        <p>
          <strong>Alcance:</strong> {detalleEvento.datosSismicos.alcanceSismo}
        </p>
      </div>

      {/* --- Botones de Herramientas --- */}
      <div className="acciones-secundarias">
        <button
          className="btn btn-secondary"
          onClick={() => alert("Visualizar en mapa no implementado")}
        >
          Visualizar en mapa 🗺️
        </button>
        <button
          className="btn btn-secondary ms-2"
          onClick={() => alert("Modificar datos sísmicos no implementado")}
        >
          Modificar datos sísmicos 🎨
        </button>
      </div>

      {/* --- Botones de Acción --- */}
      <div className="text-center acciones-finales">
        <button
          className="btn btn-warning"
          onClick={() => alert("Solicitar revisión a experto no implementado")}
          disabled={isRejecting}
        >
          Solicitar revisión a experto
        </button>
        <button
          className="btn btn-success ms-2"
          onClick={() => alert("Confirmar evento no implementado")}
          disabled={isRejecting}
        >
          Confirmar evento
        </button>
        <button 
          className="btn btn-cancelar ms-2"
          onClick={onRechazar}
          disabled={isRejecting}
        >
          {isRejecting ? "Rechazando..." : "Rechazar Evento"}
        </button>
      </div>
    </div>
  );
}