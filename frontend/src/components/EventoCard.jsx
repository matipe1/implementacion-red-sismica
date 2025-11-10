// src/components/EventoCard.jsx
import React from "react";
import "../App.css"; // para usar tus estilos globales

export default function EventoCard({ evento, onRevisar }) {
  // 🔹 Asegurar formato de fecha legible
  const fechaFormateada = evento.fechaHoraOcurrencia
    ? new Date(evento.fechaHoraOcurrencia.replace(" ", "T")).toLocaleString("es-AR")
    : "Fecha desconocida";

  return (
    <div className="evento-card">
      <h3 className="evento-titulo">
        Evento sísmico
      </h3>

      <p><strong>Fecha:</strong> {fechaFormateada}</p>
      <p><strong>Magnitud:</strong> {evento.valorMagnitud ?? "N/A"}</p>
      <p><strong>Lat. Epicentro:</strong> {evento.latitudEpicentro ?? "?"}</p>
      <p><strong>Long. Epicentro:</strong> {evento.longitudEpicentro ?? "?"}</p>
      <p><strong>Lat. Hipocentro:</strong> {evento.latitudHipocentro ?? "?"}</p>
      <p><strong>Long. Hipocentro:</strong> {evento.longitudHipocentro ?? "?"}</p>

      <button
        onClick={() => onRevisar(evento)}
        className="btn-revisar"
      >
        Revisar
      </button>
    </div>
  );
}
