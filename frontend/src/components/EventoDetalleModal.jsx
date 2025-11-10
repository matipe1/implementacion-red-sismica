import React, { useEffect, useState } from "react";
import "../App.css";

export default function EventoDetalleModal({ evento, onClose }) {
  const [detalle, setDetalle] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (evento) {
      // 🔹 Simulamos el detalle como si viniera del backend
      const detalleSimulado = {
        ...evento,
        origen: "Interplaca",
        clasificacion: "Superficial",
        alcance: "Local",
      };

      // Simulamos el retardo de la llamada al backend
      setTimeout(() => {
        setDetalle(detalleSimulado);
        setLoading(false);
      }, 1000);
    }
  }, [evento]);

  if (!evento) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
        {loading ? (
          <p className="loading">Cargando datos del evento sísmico...</p>
        ) : (
          <>
            <h2>Evento seleccionado</h2>

            <div className="event-info">
              <p>
                <strong>Ubicación:</strong> Epicentro ({detalle.latitudEpicentro},
                {detalle.longitudEpicentro}) – Hipocentro ({detalle.latitudHipocentro},
                {detalle.longitudHipocentro})
              </p>
              <p>
                <strong>Magnitud:</strong> {detalle.valorMagnitud}
              </p>
              <p>
                <strong>Fecha y hora:</strong>{" "}
                {new Date(detalle.fechaHoraOcurrencia).toLocaleString()}
              </p>
            </div>

            <h3>Datos sísmicos del evento</h3>
            <div className="event-extra">
              <p>
                <strong>Origen:</strong> {detalle.origen}
              </p>
              <p>
                <strong>Clasificación:</strong> {detalle.clasificacion}
              </p>
              <p>
                <strong>Alcance:</strong> {detalle.alcance}
              </p>
            </div>

            <div className="buttons-row">
              <button disabled className="btn-gray">
                🗺 Ver estaciones sismológicas
              </button>
              <button disabled className="btn-gray">
                ✏️ Modificar datos sísmicos
              </button>
            </div>

            <div className="actions-row">
              <button className="btn-blue">Solicitar revisión a experto</button>
              <button className="btn-blue">Confirmar evento</button>
              <button className="btn-blue">Rechazar evento</button>
              <button className="btn-red" onClick={onClose}>
                Cancelar
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
