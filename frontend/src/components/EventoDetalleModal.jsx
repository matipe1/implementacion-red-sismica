import React, { useState } from "react";
import { rechazarEvento, seleccionarEvento } from "../services/eventos.service";

export default function EventoDetalleModal({ evento, detalle, onClose }) {
  const [loading, setLoading] = useState(false);

  // 📦 Compatibilidad: el back a veces trae estos campos a nivel raíz
  // y otras veces dentro de detalle.datosSismicos
  const origen =
    detalle?.origenDeGeneracion ??
    detalle?.datosSismicos?.origenDeGeneracion ??
    "Desconocido";

  const clasificacion =
    detalle?.clasificacionSismo ??
    detalle?.datosSismicos?.clasificacionSismo ??
    "No especificada";

  const alcance =
    detalle?.alcanceSismo ??
    detalle?.datosSismicos?.alcanceSismo ??
    "No especificado";

  const handleRechazar = async () => {
    setLoading(true);
    try {
      console.log("❌ Rechazando evento desde modal:", evento);
      const res = await rechazarEvento(evento);
      console.log("📩 Respuesta del backend:", res);

      // 🔄 Consulto el detalle actualizado para mostrar el cambio de estado (incluye Rechazado)
      const detalleActualizado = await seleccionarEvento(evento);
      console.log("📦 Detalle actualizado tras rechazo:", detalleActualizado);

      // 📜 Log de todos los cambios de estado
      const cambios = detalleActualizado?.cambiosDeEstado ?? [];
      if (cambios.length) {
        console.log("📜 Cambios de estado:");
        cambios.forEach((c, i) => {
          console.log(
            `  ${i + 1}. Estado: ${c.estado}, Desde: ${c.fechaHoraDesde}, Hasta: ${c.fechaHoraHasta}`
          );
        });
      } else {
        console.log("⚠️ No se encontraron cambios de estado en la respuesta.");
      }

      alert("Evento rechazado correctamente.");
      onClose(); // cierra modal
    } catch (err) {
      console.error("🚨 Error al rechazar evento:", err.response?.data || err.message);
      alert("El servidor no permitió rechazar este evento. Verifique su estado actual.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal">
      <div className="modal-content detalle">
        <h2>Evento seleccionado</h2>

        <div className="bloque-info">
          <p>
            <strong>📍 Ubicación:</strong> Epicentro ({evento.latitudEpicentro},{" "}
            {evento.longitudEpicentro}) - Hipocentro ({evento.latitudHipocentro},{" "}
            {evento.longitudHipocentro})
          </p>
          <p>
            <strong>🌋 Magnitud:</strong> {evento.valorMagnitud}
          </p>
          <p>
            <strong>🕒 Fecha y hora:</strong>{" "}
            {new Date(evento.fechaHoraOcurrencia).toLocaleString()}
          </p>
        </div>

        <h3>Datos sísmicos del evento</h3>
        <div className="bloque-info">
          <p>
            <strong>Origen:</strong> {origen}
          </p>
          <p>
            <strong>Clasificación:</strong> {clasificacion}
          </p>
          <p>
            <strong>Alcance:</strong> {alcance}
          </p>
        </div>

        <div className="acciones">
          <button disabled title="sin funcionalidad">
            🗺 Visualizar en mapa
          </button>
          <button disabled title="sin funcionalidad">
            ✏️ Modificar datos sísmicos
          </button>
        </div>

        <div className="acciones-finales">
          <button className="btn-secundario" disabled={loading}>
            Solicitar revisión a experto
          </button>
          <button className="btn-primario" disabled={loading}>
            Confirmar evento
          </button>
          <button className="btn-primario" onClick={handleRechazar} disabled={loading}>
            {loading ? "Rechazando..." : "Rechazar evento"}
          </button>
          <button className="btn-cancelar" onClick={onClose}>
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}
